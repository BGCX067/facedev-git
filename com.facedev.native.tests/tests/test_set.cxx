/*
 * Tests for internal set implementation.
 *
 *  Created on: 10.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/test.h"
#include <fd_set.h>

using namespace fd;

TEST(add) {
	set<int> set;
	set.add(1);
	set.add(2);
	set.add(3);

	test_assert_eq(3, set.size());
}

TEST(contains) {
	set<int> set;
	set.add(1);
	set.add(2);
	set.add(3);

	test_asrt(!set.contains(0));
	test_asrt(set.contains(1));
	test_asrt(set.contains(2));
	test_asrt(set.contains(3));
	test_asrt(!set.contains(4));
}

TEST(remove) {
	set<int> set;
	set.add(1);
	set.add(2);
	set.add(3);

	test_assert_eq(3, set.size());
	test_asrt(set.contains(1));

	set.remove(1);
	test_assert_eq(2, set.size());
	test_asrt(!set.contains(1));
	test_asrt(set.contains(2));

	set.remove(1);
	test_assert_eq(2, set.size());
	test_asrt(!set.contains(1));
	test_asrt(set.contains(2));

	set.remove(2);
	test_assert_eq(1, set.size());
	test_asrt(!set.contains(1));
	test_asrt(!set.contains(2));
}


TEST(iterate) {
	set<int> set;
	set.add(1);
	set.add(2);
	set.add(3);

	iterator<int>* it = set.iterator();

	test_asrt(it->has_next());
	int i = it->next();
	test_asrt(i == 1 || i == 2 || i == 3);

	test_asrt(it->has_next());
	i = it->next();
	test_asrt(i == 1 || i == 2 || i == 3);

	test_asrt(it->has_next());
	i = it->next();
	test_asrt(i == 1 || i == 2 || i == 3);

	test_asrt(!it->has_next());
	test_asrt(!it->has_next());

	try {
		it->next();
		test_fail("Should throw an exception");
	} catch(no_such_element<int>& ex) {
	}

	delete it;
}
