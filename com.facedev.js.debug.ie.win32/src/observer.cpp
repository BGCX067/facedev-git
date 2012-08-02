/*
 * Observer class to watch over internet explorer instances
 *
 *  Created on: 31.07.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "Ocidl.h"
//#include "atlcomcli.h"


class IEObserver : IObjectWithSite {
public:
	IEObserver() {}

	virtual ~IEObserver() {}

	STDMETHOD(SetSite)(IUnknown *pUnkSite ) {
		/*CComQIPtr< IWebBrowser2 >   pWebBrowser = pUnkSite;
		if( pWebBrowser )
		{
			HWND    hWnd;
			pWebBrowser->get_HWND( (long*)&hWnd );
		}*/
		notifyOpened();
		return S_OK;
	}
private:
	void notifyOpened() {

	}
};
