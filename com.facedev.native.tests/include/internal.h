/*
 * Internal functions not intended to be calling by clients code.
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef TEST_INTERNAL_H_
#define TEST_INTERNAL_H_

#include "test.h"
#include <stdint.h>

namespace tst_inner {

/*
 * Test log line length:
 */
#define TEST_LINE_LENGTH 80

/*
 * Failed test status exit code
 */
#define EXIT_ERROR ((uint32_t)2)

/*
 * Not implemented test status exit code
 */
#define EXIT_NOT_IMPLEMENTED ((uint32_t)3)

/*
 * Structure that holds single test.
 */
typedef struct {
	const char* name;
	TestFn fn;
} Test;

/*
 * Return all the tests registered with the system.
 * Last test should is NULL for iteration purposes.
 * The resulting array is dynamically allocated and should be freed by caller.
 */
Test** getTests(void);

/*
 * Returns single test by its full name.
 */
Test* getTestByName(const char*);

/*
 * Disposes tests and resources acquired by testing engine.
 */
void disposeTests(void);

/*
 * Initializes platform with all signals listeners necessary to log failed tests output.
 * Also redirects standard output and error streams to some internal storage.
 */
void initPlatform(void);

/*
 * Executes single test with utility passed using separate process.
 * Returns test's exit status.
 */
uint32_t runTest(Test*, const char*);

/*
 * Executes single test in current process.
 * Handles result in test log properly.
 */
uint32_t runTestInCurrentProcess(Test*);

/*
 * Releases platform-specific resources including redirected stdout and stderr.
 */
void releasePlatform(void);

/*
 * Prints all collected output to the corresponding streams.
 */
void printCollectedOutput(void);

}

#endif /* TEST_INTERNAL_H_ */
