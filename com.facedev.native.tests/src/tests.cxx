/*
 ============================================================================
 Author      : Alexander Bereznevatiy
 Copyright   : (C) Alexander Bereznevatiy 2012
 Description : Tests for native facedev projects
 ============================================================================
 */

#include <stdlib.h>

#include "../include/internal.h"

using namespace tst_inner;

static const char* bs_test_command;

const char* getExecutableLocation(void) {
	return bs_test_command;
}

static int runSingleTest(const char* name) {
	Test* test = getTestByName(name);
	if (test == NULL) {
		logErr("There is no test with name %s", name);
		return EXIT_ERROR;
	}
	int result = runTestInCurrentProcess(test);
	disposeTests();
	return result;
}


static int runMultipleTests(const char* command) {
	logMsg("Executing unit tests for facedev:");

	Test** tests = getTests();
	if (tests == NULL || tests[0] == NULL) {
		logMsg("(no tests to run)");
		return EXIT_SUCCESS;
	}

	unsigned int passed = 0, failed = 0, errors = 0, notImplemented = 0;

	int i;
	for (i = 0; tests[i] != NULL; i++) {
		uint32_t status = runTest(tests[i], command);
		if (status == EXIT_SUCCESS) {
			passed++;
		} else if (status == EXIT_NOT_IMPLEMENTED) {
			notImplemented++;
		} else if (status == EXIT_ERROR) {
			errors++;
		} else {
			failed++;
		}
	}

	/*
	 * Dispose resources:
	 */
	free(tests);
	disposeTests();

	logMsg("%d tests run, %d passed, %d failed, %d errors, %d not implemented \n",
			passed + failed + errors + notImplemented, passed, failed, errors, notImplemented);

	return failed > 0 || errors > 0 ? EXIT_FAILURE : EXIT_SUCCESS;
}

int main(int argc, char** argv) {
	if (argc == 0) {
		logErr("No arguments passed - this implementation expect executable name to be passed.");
		return EXIT_ERROR;
	}
	bs_test_command = argv[0];

	if (argc > 1) {
		return runSingleTest(argv[1]);
	}

	return runMultipleTests(argv[0]);
}
