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

namespace fd {
/*
 * Returns greater argument of the first one if arguments are equal.
 */
template <typename T>
inline T max(T a, T b) {
	return a < b ? b : a;
}

} //namespace fd {

/*
 * Logs message in standard output for debug purposes.
 */
#define fd_debug(msg, ...) do { printf("[DEBUG] "); printf((msg), ## __VA_ARGS__); puts(""); fflush(stdout);} while(0)

/*
 * Logs message in standard output for informational purposes.
 */
#define fd_info(msg, ...) do { printf("[INFO] "); printf((msg), ## __VA_ARGS__); puts(""); fflush(stdout);} while(0)

/*
 * Logs error message in standard output.
 */
#define fd_error(msg, ...) do { printf("[ERROR] "); printf((msg), ## __VA_ARGS__); puts(""); fflush(stdout);} while(0)

#endif /* FD_COMMON_H_ */
