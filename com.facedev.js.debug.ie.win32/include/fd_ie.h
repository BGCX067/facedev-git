/*
 * Provides API for BHO that is used by bridge.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_IE_H_
#define FD_IE_H_

#include <string>

class fd_bridge;

/*
 * Represents browser instance (window or tab).
 */
class fd_ieinstance {
private:
	std::string _name;

	friend class fd_bridge;

	fd_ieinstance(std::string name) {
		this->_name = name;
	}

	virtual ~fd_ieinstance(){}
public:
	inline const std::string name() {
		return _name;
	}
};


#endif /* FD_IE_H_ */
