/*
 * StringUtil.h
 *
 *  Created on: 18 Aug 2017
 *      Author: Walter Fan
 */

#ifndef WALTER_STRING_UTIL_H_
#define WALTER_STRING_UTIL_H_

#include "regex.h"
#include <string>
#include <vector>
#include <sstream>

namespace wfutil {

template <typename T1, typename T2>
T2 ConvertDataType(T1 src)
{
    T2 dest;
    std::stringstream sStrStr;
    sStrStr << src;
    sStrStr >> dest;
    return dest;
}


struct lt_strcasecmp {
    bool operator()(const char * s1, const char * s2) const {
    		std::string strSrc(s1);
    		std::string strDest(s2);

		return strSrc < strDest;
    }
};

typedef enum {
    wf_result_ok = 0,
    wf_result_error = 1,
    wf_result_ignore = 2,
    wf_result_not_found = 3,
    wf_result_null_ptr = 4

} wf_result_t;


class StringUtil {
public:
    int split(std::string strValue, std::string separator, std::vector<std::string>& strArr);

    static bool match(const char *string, const char *pattern);

    static wf_result_t get_value_by_key(const std::string& strMsml, std::string &strValue, const char* key, const char* sep);

    static wf_result_t replace_value_by_key(std::string& strMsml, std::string strValue, const char* key, const char* sep);
};

}
#endif
