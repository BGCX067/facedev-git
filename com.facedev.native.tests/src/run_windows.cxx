/*
 * Tests runner implementation. Basic idea is to execute each test in separate thread.
 * This will protect tests from each other. This implementation works only for Windows.
 * 
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/internal.h"

#ifdef WIN32

#include <windows.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

using namespace tst_inner;

uint32_t tst_inner::runTest(Test* test, const char* command) {
	STARTUPINFO startupInfo;
	PROCESS_INFORMATION processInfo;

	ZeroMemory(&startupInfo, sizeof(startupInfo));
	startupInfo.cb = sizeof(startupInfo);
	ZeroMemory(&processInfo, sizeof(processInfo));

	char* cmdLine = (char*)malloc(sizeof(char) * (strlen(command) + strlen(test->name) + 5));
	if (cmdLine == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}
	sprintf(cmdLine, "%s %s", command, test->name);

	if (!CreateProcess(NULL, cmdLine, NULL, NULL, FALSE, 0, NULL, NULL,
			&startupInfo, &processInfo)) {
		logErr("Unable to run the test %s because process creation failed (code %d).", test->name, GetLastError());
		return EXIT_ERROR;
	}
	// Wait until child process exits.
	WaitForSingleObject(processInfo.hProcess, INFINITE);

	DWORD exitCode = 0;
	if (!GetExitCodeProcess(processInfo.hProcess, &exitCode)) {
		logErr("Unable to get %s test exit code.", test->name);
		return EXIT_ERROR;
	}

	// Disposing resources:
	CloseHandle(processInfo.hProcess);
	CloseHandle(processInfo.hThread);
	free(cmdLine);

	if (exitCode > __INT32_MAX__) {
		return EXIT_ERROR;
	}

	return (uint32_t) exitCode;
}

/*****************************************************************************


*****************************************************************************/

/*
 * These variables holds pipes to stdout and stderr streams.
 * All tests output is stored in here.
 */
static int stdoutPipe[2];
static int stderrPipe[2];


/*
 * These variable holds old values for stdout and stderr (saved before test run).
 *
 */
static int stdoutHolder;
static int stderrHolder;

/*
 * 4MB for temp pipe.
 * TODO: Not universal: replace with proper thread.
 */
#define BS_STD_PIPE_SIZE ((unsigned int)1 << 22)



LONG WINAPI crashFilter(EXCEPTION_POINTERS *);




void tst_inner::initPlatform(void) {
	SetUnhandledExceptionFilter(crashFilter);

	fflush(stdout);
	fflush(stderr);

	stdoutHolder = dup(STDOUT_FILENO);
	stderrHolder = dup(STDERR_FILENO);

	if (_pipe(stdoutPipe, BS_STD_PIPE_SIZE, O_TEXT) != 0 ) {
		logErr("Error creating pipe!");
		exit(EXIT_ERROR);
	}

	if (_pipe(stderrPipe, BS_STD_PIPE_SIZE, O_TEXT) != 0 ) {
		logErr("Error creating pipe!");
		exit(EXIT_ERROR);
	}

	/*
	 * Redirecting output:
	 */
	dup2(stdoutPipe[1], STDOUT_FILENO);
	close(stdoutPipe[1]);

	dup2(stderrPipe[1], STDERR_FILENO);
	close(stderrPipe[1]);
}




void tst_inner::releasePlatform(void) {
	// TODO: release unhandled exception hander

	fflush(stdout);
	fflush(stderr);

	dup2(stdoutHolder, STDOUT_FILENO);
	dup2(stderrHolder, STDERR_FILENO);
}




static void flushPipe(FILE* target, int pipe, const char* message) {
	static const size_t bufSize = 1024;
	char buf[bufSize];
	size_t size;
	unsigned char messagePrinted = 0;
	while ((size = read(pipe, buf, bufSize)) > 0) {
		if (!messagePrinted) {
			puts(message);
			fflush(stdout);
			messagePrinted = 1;
		}
		size_t i = 0;
		while (i < size) {
			putc(buf[i++], target);
		}
	}
	if (messagePrinted) {
		putc('\n', target);
	}
	fflush(target);
}




void tst_inner::printCollectedOutput(void) {
	flushPipe(stdout, stdoutPipe[0], "stdout:");
	flushPipe(stderr, stderrPipe[0], "stderr:");
}




LONG WINAPI crashFilter(EXCEPTION_POINTERS * info) {
	const char* msg;
	switch (info->ExceptionRecord->ExceptionCode) {
	case EXCEPTION_ACCESS_VIOLATION:
		msg = "Access violation";
		break;
	case EXCEPTION_ARRAY_BOUNDS_EXCEEDED:
		msg = "Array index out of bounds";
		break;
	case EXCEPTION_BREAKPOINT:
		msg = "A breakpoint was encountered.";
		break;
	case EXCEPTION_DATATYPE_MISALIGNMENT:
		msg = "Wrong-aligned data!";
		break;
	case EXCEPTION_FLT_DENORMAL_OPERAND:
		msg = "One of the operands in a floating-point operation is denormal.";
		break;
	case EXCEPTION_FLT_DIVIDE_BY_ZERO:
		msg = "Divide by zero";
		break;
	case EXCEPTION_FLT_INEXACT_RESULT:
		msg = "Floating operation inexact result";
		break;
	case EXCEPTION_FLT_INVALID_OPERATION:
		msg = "Invalid floating operation";
		break;
	case EXCEPTION_FLT_OVERFLOW:
		msg = "Floating point overflow";
		break;
	case EXCEPTION_FLT_STACK_CHECK:
		msg = "The stack overflowed or underflowed as the result of a floating-point operation.";
		break;
	case EXCEPTION_FLT_UNDERFLOW:
		msg = "The exponent of a floating-point operation is less than the magnitude allowed by the corresponding type.";
		break;
	case EXCEPTION_ILLEGAL_INSTRUCTION:
		msg = "Illegal instruction";
		break;
	case EXCEPTION_IN_PAGE_ERROR:
		msg = "Page is not exists or exception due page loading";
		break;
	case EXCEPTION_INT_DIVIDE_BY_ZERO:
		msg = "Integer divided by zero!";
		break;
	case EXCEPTION_INT_OVERFLOW:
		msg = "Integer overflow";
		break;
	case EXCEPTION_INVALID_DISPOSITION:
		msg = "Invalid disposition";
		break;
	case EXCEPTION_NONCONTINUABLE_EXCEPTION:
		msg = "Trying to continue non-continuable exception";
		break;
	case EXCEPTION_PRIV_INSTRUCTION:
		msg = "Invalid instruction operation";
		break;
	case EXCEPTION_SINGLE_STEP:
		msg = "Trace trap or other single-instruction mechanism signaled that one instruction has been executed.";
		break;
	case EXCEPTION_STACK_OVERFLOW:
		msg = "Stack overflow";
		break;
	default:
		msg = "Unknown signal";
		break;
	}

	releasePlatform();

	puts(msg);

	printCollectedOutput();

	exit(EXIT_ERROR);

	return 0;// to avoid compilation warning
}

#endif // #ifdef WIN32
