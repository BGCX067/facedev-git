/*
 * Provides API for BHO that is used by bridge.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_IE_H_
#define FD_IE_H_

#include <string>

namespace fd {

class bridge;

/*
 * Represents browser instance (window or tab).
 */
class ieinstance {
private:
	std::wstring _name;

	friend class fd::bridge;

	ieinstance(std::wstring name) {
		this->_name = name;
	}

	virtual ~ieinstance(){}
public:
	inline const std::wstring name() {
		return _name;
	}
};

} // namespace fd

#endif /* FD_IE_H_ */
