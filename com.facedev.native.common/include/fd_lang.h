/*
 * Language common routines and definitions.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_LANG_H_
#define FD_LANG_H_

#include <stdint.h>

#define fd_null 0


typedef uint32_t fd_uint;

typedef uint64_t fd_ulong;

typedef uint8_t fd_bool;

#define fd_true ((fd_bool)1)

#define fd_false ((fd_bool)0)

#define fd_static_assert(e)                             \
    do {                                                \
        enum { fd_static_assert_was_failed__ = 1/(e) }; \
    } while (0)

#endif /* FD_LANG_H_ */
