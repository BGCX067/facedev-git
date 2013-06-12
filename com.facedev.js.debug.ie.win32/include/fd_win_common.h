/*
 * Provides common functions for windows.
 *
 *  Created on: 26.03.2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_WIN_COMMON_H_
#define FD_WIN_COMMON_H_

#include "../../com.facedev.native.common/include/fd_common.h"

/*
 * Checks HRESULT for error. If there is an error in HRESULT value - logs it and returns false.
 * Otherwise returns true.
 */
fd_bool fd_win_check_n_log(HRESULT);


#endif /* FD_WIN_COMMON_H_ */
