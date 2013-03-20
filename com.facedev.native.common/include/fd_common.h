/*
 * Common native utilities for facedev.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_COMMON_H_
#define FD_COMMON_H_

#include "fd_lang.h"
#include <stdio.h>

/*
 * Returns greater argument of the first one if arguments are equal.
 */
template <typename T>
inline T fd_max(T a, T b) {
	return a < b ? b : a;
}

/*
 * Logs message in standard output for debug purposes.
 */
#define fd_log(msg, ...) do { printf((msg), ## __VA_ARGS__); puts(""); } while(0)

#endif /* FD_COMMON_H_ */
