/*
 * Tests for observer class.
 *
 *  Created on: 08.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/test.h"
#include "../../com.facedev.native.common/include/fd_observer.h"

using namespace fd;

static int counter = 0;

static void increment(int size) {
	counter += size;
}

static int value = 0;

static void setme(int val) {
	value = val;
}

TEST(listen) {
	counter = 0;
	value = 0;

	observer<int> obs;

	obs.listen(increment);
	obs.fire(2);

	test_assert_eq(2, counter);
	test_assert_eq(0, value);

	obs.fire(3);

	test_assert_eq(5, counter);
	test_assert_eq(0, value);

	obs.listen(setme);

	obs.fire(5);

	test_assert_eq(10, counter);
	test_assert_eq(5, value);
}

TEST(unlisten) {
	counter = 0;
	value = 0;

	observer<int> obs;

	obs.listen(setme);
	obs.listen(increment);

	obs.fire(3);

	test_assert_eq(3, counter);
	test_assert_eq(3, value);

	obs.unlisten(setme);

	obs.fire(5);

	test_assert_eq(8, counter);
	test_assert_eq(3, value);

	obs.unlisten(increment);

	obs.fire(1);

	test_assert_eq(8, counter);
	test_assert_eq(3, value);
}

