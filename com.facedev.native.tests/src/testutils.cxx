/*
 * Additional utilities for tests implementation.
 *
 *  Created on: 15.06.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/testutils.h"

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include <string.h>

void logErr(const char* msg, ...) {
	va_list argptr;

	va_start(argptr, msg);

	vfprintf(stderr, msg, argptr);
	fputs("\n", stderr);
	fflush(stderr);

	va_end(argptr);
}

void logMsg(const char* msg, ...) {
	va_list argptr;

	va_start(argptr, msg);

	vfprintf(stdout, msg, argptr);
	fputs("\n", stdout);
	fflush(stdout);

	va_end(argptr);

}

const char* resolveFileName(const char* path) {
	const char* base = getExecutableLocation();

	int i = strlen(base), cnt = 0;
	while (cnt < 3 && i --> 0) {
		if (base[i] == '/' || base[i] == '\\') {
			cnt ++;
		}
	}

	int len = strlen(path);
	char* result;
	if (cnt == 3) {
		const char* projDir = "bstests/";
		result = (char*) malloc(i + len + 2 + strlen(projDir));
		if (result == NULL) {
			logErr("Out of memory!");
			exit(EXIT_FAILURE);
		}
		strncpy(result, base, i);
		result[i++] = '/';
		result[i] = 0;
		strcat(result, projDir);
	} else {
		result = (char*) malloc(len + 1);
		if (result == NULL) {
			logErr("Out of memory!");
			exit(EXIT_FAILURE);
		}
		result[0] = 0;
	}
	strcat(result, path);

	printf("%s\n", result);

	return result;
}
