/*
 * Platform-specific implementation of tests runner for MacOS.
 * Since MacOS is POSIX system, uses fork() to protect tests address space.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */


#include "../include/internal.h"

#ifdef __APPLE__

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <signal.h>


using namespace tst_inner;


uint32_t tst_inner::runTest(Test* test, const char* command) {
	pid_t pid = fork();

	if (pid < 0) {
		return EXIT_ERROR;
	} else if (pid == 0) {
		uint32_t result = runTestInCurrentProcess(test);
		disposeTests();
		exit(result);
	} else {
		int result = 0;
		waitpid(pid, &result, WCONTINUED | WUNTRACED);
		return result < 0 ? EXIT_ERROR : WEXITSTATUS(result);
	}
}




/*
 * Error handling function for signals.
 */
static void catchSignal(int signal) {
	const char* msg;
	switch (signal) {
		case SIGILL:
			msg = "Illegal instruction";
			break;
		case SIGABRT:
			msg = "Abort";
			break;
		case SIGTRAP:
			msg = "Trace trap";
			break;
		case SIGFPE:
			msg = "Floating-point exception";
			break;
		case SIGBUS:
			msg = "Bus error";
			break;
		case SIGSEGV:
			msg = "Segmentation violation";
			break;
		case SIGSYS:
			msg = "Bad argument to system call";
			break;
		case SIGPIPE:
			msg = "Broken pipe";
			break;
		case SIGALRM:
			msg = "Alarm clock";
			break;
		case SIGTERM:
			msg = "Termination";
			break;
		case SIGUSR1:
			msg = "User signal 1";
			break;
		case SIGUSR2:
			msg = "User signal 2";
			break;
		case SIGCHLD:
			msg = "Child status has changed";
			break;
		case SIGXCPU:
			msg = "Exceeded CPU time";
			break;
		default:
			msg = "Unknown signal";
			break;
	}

	releasePlatform();

	puts(msg);

	printCollectedOutput();

	exit(EXIT_ERROR);
}




/*
 * Attach and detach signals macro-functions.
 */
#define SIG_ATTACH(sig) do { if (signal((sig), catchSignal) == SIG_ERR) {        \
	logErr("Error while attaching signal %s", #sig);                             \
	exit(EXIT_ERROR);                                                            \
} } while(0)

#define SIG_DETACH(sig) do { if (signal((sig), SIG_DFL) == SIG_ERR) {            \
	logErr("Error while detaching signal %s", #sig);                             \
	exit(EXIT_ERROR);                                                            \
} } while(0)



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




void tst_inner::initPlatform(void) {
	SIG_ATTACH(SIGILL);
	SIG_ATTACH(SIGABRT);
	SIG_ATTACH(SIGTRAP);
	SIG_ATTACH(SIGIOT);
	SIG_ATTACH(SIGFPE);
	SIG_ATTACH(SIGBUS);
	SIG_ATTACH(SIGSEGV);
	SIG_ATTACH(SIGSYS);
	SIG_ATTACH(SIGPIPE);
	SIG_ATTACH(SIGALRM);
	SIG_ATTACH(SIGTERM);
	SIG_ATTACH(SIGUSR1);
	SIG_ATTACH(SIGUSR2);
	SIG_ATTACH(SIGCHLD);
	SIG_ATTACH(SIGXCPU);

	fflush(stdout);
	fflush(stderr);

	stdoutHolder = dup(STDOUT_FILENO);
	stderrHolder = dup(STDERR_FILENO);

	if (pipe(stdoutPipe) != 0 ) {
		logErr("Error creating pipe!");
		exit(EXIT_ERROR);
	}

	if (pipe(stderrPipe) != 0 ) {
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
	SIG_DETACH(SIGILL);
	SIG_DETACH(SIGABRT);
	SIG_DETACH(SIGTRAP);
	SIG_DETACH(SIGIOT);
	SIG_DETACH(SIGFPE);
	SIG_DETACH(SIGBUS);
	SIG_DETACH(SIGSEGV);
	SIG_DETACH(SIGSYS);
	SIG_DETACH(SIGPIPE);
	SIG_DETACH(SIGALRM);
	SIG_DETACH(SIGTERM);
	SIG_DETACH(SIGUSR1);
	SIG_DETACH(SIGUSR2);
	SIG_DETACH(SIGCHLD);
	SIG_DETACH(SIGXCPU);

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
	// Flushing stderr to stdout because of in other case sequence of output will be broken
	flushPipe(stdout, stderrPipe[0], "stderr:");
}

#endif // #ifdef __APPLE__

