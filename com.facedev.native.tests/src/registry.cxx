/*
 * Tests registry functions implementation.
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/internal.h"

#include <stdlib.h>
#include <string.h>
#include <stdint.h>

using namespace tst_inner;

/*
 * Internal holder for tests.
 */
typedef struct _TestHolder {
	struct _TestHolder* next;
	Test* test;
} TestHolder;

/*
 * Default size of tests map. Will be doubled each time when rehashing is required.
 */
#define TESTS_MAP_SIZE_DEFAULT ((size_t)64)
/*
 * Collection of tests and the size of the collection are the global variables.
 * Collection is represented as hash map with key equals to test name.
 * Chaining is used as collision resolution technique.
 */
static TestHolder** testsMap = NULL;

static size_t testsCount = 0;

static size_t testsMapSize = 0;




/*
 * String hash function. Creates quick hash for test name.
 * This realization uses FNV algorithm.
 * See http://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function
 * This implementation uses 32-bits prime and offset and produces 32-bits hash.
 */
#define HASH_FNV_PRIME ((uint32_t)33554393)
#define HASH_FNV_BASE ((uint32_t)2147483579)

static uint32_t hashTestName(const char* name) {
	if (name == NULL) {
		return 0;
	}
	uint32_t hash = HASH_FNV_BASE;

	char* iterator = (char*)name;

	while (*iterator) {
		hash *= HASH_FNV_PRIME;
		hash ^= ((uint32_t)*(iterator++));
	}
	return hash % testsMapSize;
}




/*
 * Comparison function for tests. Compares tests by their names.
 */
static int compareTests(const Test** first, const Test** second) {
	return strcmp((*first)->name, (*second)->name);
}




Test** tst_inner::getTests(void) {
	const size_t length = testsCount + 1;
	Test** result = (Test**)malloc(sizeof(Test*) * length);

	if (result == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}

	size_t i = 0, j = 0;

	TestHolder** iterator = testsMap;

	if (testsMap == NULL) {
		result[i] = NULL;
		return result;
	}

	while (j++ < testsMapSize) {
		TestHolder* holder = *(iterator++);
		while (holder != NULL) {
			result[i++] = holder->test;
			holder = holder->next;
		}
	}

	result[i] = NULL;

	qsort(result, testsCount, sizeof(Test*), (int (*)(const void*,const void*))compareTests);

	return result;
}




Test* tst_inner::getTestByName(const char* name) {
	uint32_t hash = hashTestName(name);
	TestHolder* holder = testsMap[hash];

	while (holder != NULL) {
		if (strcmp(holder->test->name, name) == 0) {
			return holder->test;
		}
		holder = holder->next;
	}
	return NULL;
}




static void rebuildHashTable() {
	if (testsMap == NULL) {
		testsMapSize = TESTS_MAP_SIZE_DEFAULT;
	} else {
		testsMapSize <<= 1;
	}
	TestHolder** newMap = (TestHolder**) malloc(sizeof(TestHolder*) * testsMapSize);

	if (newMap == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}

	memset(newMap, 0, sizeof(TestHolder*) * testsMapSize);

	if (testsMap == NULL) {
		testsMap = newMap;
		return;
	}

	exit(EXIT_FAILURE);
}




/*
 * Creates test name based on folder and file where test is located
 */
static const char* createTestName(const char* name, const char* fileName) {
	const size_t baseLength = strlen(__FILE__) - strlen("src/registry.cxx") + strlen("tests/");

	const size_t fileNameLength = strlen(fileName);

	if (fileNameLength <= baseLength) {
		char* nameCopy = (char*) malloc(sizeof(char) * (strlen(name) + 1));
		if (nameCopy == NULL) {
			logErr("Out of memory!");
			exit(EXIT_ERROR);
		}
		strcpy(nameCopy, name);
		return nameCopy;
	}

	char* nameBuf = (char*) malloc(sizeof(char) * (fileNameLength - baseLength + strlen(name) + 2));
	if (nameBuf == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}

	size_t i = 0,
		j = baseLength - 1,
		lastSlash = -1;

	while (++j < fileNameLength) {
		if (fileName[j] == '\\' || fileName[j] == '/') {
			lastSlash = i++;
			nameBuf[lastSlash] = '.';
		} else {
			nameBuf[i++] = fileName[j];
		}
	}
	nameBuf[i] = 0;
	size_t lastDot = strrchr(nameBuf, '.') - nameBuf;

	if (lastDot > lastSlash) {
		i = lastDot;
	}

	nameBuf[i++] = '.';
	nameBuf[i] = 0;
	strcat(nameBuf, name);

	return nameBuf;
}




void tst_inner::regTest(const char* name, const char* fileName, int line, TestFn fn) {
	if (testsMap == NULL || testsCount > testsMapSize) {
		rebuildHashTable();
	}
	TestHolder* holder = (TestHolder*) malloc(sizeof(TestHolder));
	if (holder == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}

	Test* test = (Test*) malloc(sizeof(Test));
	if (test == NULL) {
		logErr("Out of memory!");
		exit(EXIT_ERROR);
	}

	/*
	 * Create test and insert it into hashtable:
	 */
	test->name = createTestName(name, fileName);
	test->fn = fn;

	holder->test = test;

	uint32_t hash = hashTestName(test->name);

	holder->next = testsMap[hash];
	testsMap[hash] = holder;

	testsCount ++;
}




void tst_inner::disposeTests(void) {
	if (testsMap == NULL) {
		return;
	}
	size_t i = 0;
	while (i < testsMapSize) {
		TestHolder* iterator = testsMap[i++];

		while (iterator != NULL) {
			free((void*)iterator->test->name);
			free(iterator->test);
			TestHolder* holder = iterator;
			iterator = iterator->next;
			free(holder);
		}
	}

	free(testsMap);
	testsMap = NULL;
}
