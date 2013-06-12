/*
 * Support utilities for tests.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef TEST_UTILS_H_
#define TEST_UTILS_H_

#include <stddef.h>

/*
 * NULL type of tests:
 */
#ifndef NULL
#define NULL ((void *)0)
#endif

/*
 * Resolves folder based on root of tests project name.
 * First parameter is relative path to the root of the bstests project.
 */
const char* resolveFileName(const char*);

/*
 * Returns location of tests executable.
 */
const char* getExecutableLocation(void);

/*
 * Writes a message in the tests log.
 */
void logMsg(const char*, ...);

/*
 * Writes error to standard error stream.
 */
void logErr(const char*, ...);


#endif /* TEST_UTILS_H_ */
