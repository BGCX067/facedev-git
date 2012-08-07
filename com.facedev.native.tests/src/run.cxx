/*
 * Common test runner implementation.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/internal.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

using namespace tst_inner;

uint32_t tst_inner::runTestInCurrentProcess(Test* test) {
	printf("%s", test->name);
	int i = strlen(test->name);
	while (i++ < TEST_LINE_LENGTH) {
		putchar('.');
	}

	initPlatform();

	uint32_t result = EXIT_SUCCESS;
	const char* msg = "OK";
	try {
		test->fn();
	} catch (assertion_error* ex) {
		result = EXIT_FAILURE;
		msg = ex->message();
	} catch (not_implemented* ex) {
		result = EXIT_NOT_IMPLEMENTED;
		msg = "Not implemented";
	} catch (...) {
		msg = "Unknown exception";
		result = EXIT_ERROR;
	}


	releasePlatform();

	puts(msg);

	if (result != EXIT_SUCCESS) {
		printCollectedOutput();
	}

	return result;
}
