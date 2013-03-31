/*
 * Provides API for BHO that is used by bridge.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_IE_H_
#define FD_IE_H_

#include "../../com.facedev.native.common/include/fd_lang.h"
#include <string>
#include <windows.h>

namespace fd {

class bridge;

/*
 * Represents browser instance (window or tab).
 */
class ieinstance {
private:
	fd_ulong _id;
	std::wstring _name;

	friend class fd::bridge;

	ieinstance(fd_ulong id, std::wstring name) {
		_id = id;
		this->_name = name;
	}

	virtual ~ieinstance(){}
public:
	inline const std::wstring name() {
		return _name;
	}

	inline const fd_ulong id() {
		return _id;
	}
};

} // namespace fd

#endif /* FD_IE_H_ */
