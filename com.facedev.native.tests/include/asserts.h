/*
 * Asserts useful for testing
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef TEST_ASSERTS_H_
#define TEST_ASSERTS_H_

namespace tst_inner {

class assertion_error {
private:
	const char* _message;
	const char* _file;
	int _line;
public:
	assertion_error(const char* message, const char* file, int line) {
		this->_message = message;
		this->_file = file;
		this->_line = line;
	}

	~assertion_error() {}

	const char* message() {
		return _message;
	}
};

class not_implemented {
public:
	not_implemented() {}
	~not_implemented() {}
};

}

/*
 * Fast shorthand for test_assert where string presentation of test is used as message.
 */
#define test_asrt(test) do {                          \
	if (!(test))                                      \
		throw new tst_inner::assertion_error(         \
			#test" is false", __FILE__, __LINE__);    \
	} while(0)

/*
 * This one is similar to minunit. See http://www.jera.com/techinfo/jtns/jtn002.html
 */
#define test_assert(message, test) do {               \
	if (!(test))                                      \
		throw new tst_inner::assertion_error(         \
			message, __FILE__, __LINE__);             \
	} while (0)

/*
 * Provides assert equals functionality.
 */
#define test_assert_eq(expected, actual) do {         \
	if ((expected) != (actual))                       \
		throw new tst_inner::assertion_error(         \
			"Expected "#expected, __FILE__, __LINE__);\
	} while(0)

/*
 * Fails test with message specified.
 */
#define test_fail(message) throw new tst_inner::assertion_error(message, __FILE__, __LINE__)

/*
 * Signalizes that test is not implemented yet.
 */
#define test_not_implemented() throw new tst_inner::not_implemented()

#endif /* TEST_ASSERTS_H_ */
