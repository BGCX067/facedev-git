/*
 * Basic test functions. This is only header you need to include in your tests.
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_TEST_H_
#define FD_TEST_H_

#include "testutils.h"
#include "asserts.h"

/*****************************************************************
 *                     PUBLIC TESTING API
 *****************************************************************/

/*
 * Macro expression to initiate and self-register tests.
 * parameter should be a test name (unique within current file).
 * Usage is following:
 *  TEST(my_test_name) {
 *  	// checking code is here
 *  }
 */
#define TEST(name) static void name();                         \
	__attribute__ ((__constructor__))                          \
	static void __BS__REG_FUNCTION(__LINE__)() {               \
		tst_inner::regTest(#name, __FILE__, __LINE__, name);   \
	}                                                          \
static void name()

/*****************************************************************
 *               INTERNAL FUNCTIONS AND STRUCTURES
 *****************************************************************/

namespace tst_inner {
// internal supplementary expressions:

#define __BS__MERGE_MACRO(x, y) x##y
#define __BS__REG_FUNCTION(uid) __BS__MERGE_MACRO($___registerFn__, uid)

/*
 * Function ally for convenience:
 */
typedef void (*TestFn)(void);

/*
 * Registers test with name specified for filename and line number provided and function to call for test.
 */
void regTest(const char*, const char*, int, TestFn);

};

#endif /* BS_TEST_H_ */
