/*
 * Tests for fd_map implementation.
 *
 *  Created on: 09.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */


#include "../include/test.h"
#include <fd_map.h>
#include <string.h>

using namespace fd;

TEST(put) {
	map<const char*, int> map;

	map.put("aaa", 4);
	test_assert_eq(1, map.size());

	map.put("bbb", 111);
	test_assert_eq(2, map.size());

	map.put("ccc", -1);
	test_assert_eq(3, map.size());
}


TEST(get) {
	map<const char*, int> map;

	map.put("aaa", 4);
	map.put("bbb", 111);
	map.put("ccc", -1);

	test_assert_eq(4, map.get("aaa"));
	test_assert_eq(111, map.get("bbb"));
	test_assert_eq(-1, map.get("ccc"));
	test_asrt(!map.has("ddd"));
}


TEST(remove) {
	map<const char*, int> map;

	map.put("aaa", 4);
	map.put("bbb", 111);
	map.put("ccc", -1);

	test_assert_eq(4, map.remove("aaa"));
	try {
		map.get("aaa");
	} catch (no_such_element<const char*>& ex) {
		test_asrt(strcmp("aaa", ex.element()) == 0);
	}
	test_assert_eq(2, map.size());

	test_assert_eq(111, map.remove("bbb"));
	test_asrt(!map.has("bbb"));
	test_assert_eq(1, map.size());

	test_assert_eq(-1, map.remove("ccc"));
	test_asrt(!map.has("ccc"));
	test_assert_eq(0, map.size());
}

TEST(rehash) {
	map<const char*, int> map(10);

	map.put("aaa", 4);
	map.put("bbb", 111);
	map.put("ccc", -1);
	map.put("a", 2);
	map.put("b", 3);
	map.put("c", 4);
	map.put("d", 5);
	map.put("e", 6);
	map.put("f", 7);
	map.put("g", 8);
	map.put("h", 9);
	map.put("i", 10);
	map.put("j", 11);

	test_assert_eq(13, map.size());

	test_assert_eq(4, map.get("aaa"));
	test_assert_eq(111, map.get("bbb"));
	test_assert_eq(-1, map.get("ccc"));
	test_assert_eq(2, map.get("a"));
	test_assert_eq(3, map.get("b"));
	test_assert_eq(4, map.get("c"));
	test_assert_eq(5, map.get("d"));
	test_assert_eq(6, map.get("e"));
	test_assert_eq(7, map.get("f"));
	test_assert_eq(8, map.get("g"));
	test_assert_eq(9, map.get("h"));
	test_assert_eq(10, map.get("i"));
	test_assert_eq(11, map.get("j"));
}

TEST(keys) {
	map<const char*, int> map(10);

	map.put("aaa", 4);
	map.put("bbb", 111);
	map.put("ccc", -1);

	const char** keys = map.keys();

	test_asrt(strcmp("aaa", keys[0]) == 0 ||
			strcmp("bbb", keys[0]) == 0 ||
			strcmp("ccc", keys[0]) == 0);

	delete[] keys;
}

TEST(values) {
	map<const char*, int> map(10);

	map.put("aaa", 4);
	map.put("bbb", 111);
	map.put("ccc", -1);

	int* values = map.values();

	test_asrt(values[0] == 4 || 111 == values[0] ||-1 == values[0]);

	delete[] values;
}
