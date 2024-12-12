
#include "StringUtil.h"

using namespace std;

namespace wfutil {


bool StringUtil::match(const char *string, const char *pattern)
{
    int    status;
    regex_t    re;

    if (regcomp(&re, pattern, REG_EXTENDED|REG_NOSUB) != 0) {
        return false;
    }

    status = regexec(&re, string, (size_t) 0, NULL, 0);
    regfree(&re);

    if (status != 0) {
        return false;
    }
    return true;
}

int StringUtil::split(string strValue, string separator, vector<string>& strArr)
{
    if(strValue.empty())
        return 0;
    
    string::size_type pos0 = 0;
    string::size_type pos1 = strValue.find(separator, pos0);
    string::size_type seplen = separator.size();

    int cnt = 0;
    while(string::npos != pos1) {
        strArr.push_back(strValue.substr(pos0, pos1-pos0));
        cnt++;
        pos0 = pos1 + seplen;
        pos1 = strValue.find(separator, pos0);
    }
    
    if(pos1 > pos0) {
        strArr.push_back(strValue.substr(pos0, pos1 - pos0));
        cnt++;
    }

    return cnt;
}

wf_result_t StringUtil::get_value_by_key(const std::string& strMsml, string &strValue, const char* key, const char* sep) {
    if(strMsml.empty()) return wf_result_not_found;

    std::string::size_type nBegin = strMsml.find(key);
    int nKeyLen = strlen(key);
    if(string::npos == nBegin) return wf_result_not_found;

    std::string::size_type nEnd = strMsml.find_first_of(sep, nBegin+nKeyLen);

    if(std::string::npos == nEnd) {
        return wf_result_not_found;
    }

    strValue = strMsml.substr(nBegin + nKeyLen, nEnd - nBegin - nKeyLen);

    return wf_result_ok;
}

wf_result_t StringUtil::replace_value_by_key(std::string& strMsml, string strValue, const char* key, const char* sep) {

    std::string::size_type nBegin = strMsml.find(key);
    if(string::npos == nBegin) return wf_result_not_found;
    int nKeyLen = strlen(key);
    std::string::size_type nEnd = strMsml.find_first_of(sep, nBegin + nKeyLen);

    if(std::string::npos == nEnd) {
        strMsml  = strMsml.substr(0, nBegin) + key + strValue;
    } else {
        strMsml = strMsml.substr(0, nBegin) + key + strValue + strMsml.substr(nEnd);
    }

    return wf_result_ok;
}

}