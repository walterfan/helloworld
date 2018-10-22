#include "gtest/gtest.h"
#include "gmock/gmock.h"
#include "StringUtil.h"

using namespace std;

using namespace wfutil;

TEST(StringUtilTest, replacePinValue) {
    string strBody = "<name>ping</name>\r\n<value>6666#</value>\r\n";

    string strPinValue = "3344#";
    wf_result_t ret1 = StringUtil::replace_value_by_key(strBody, strPinValue,  "<value>", "</");
    EXPECT_EQ(ret1, wf_result_ok);

    string strReplacedPin = "";

    wf_result_t ret2 = StringUtil::get_value_by_key(strBody, strReplacedPin, "<value>", "</");
    EXPECT_EQ(ret2, wf_result_ok);
    EXPECT_EQ("3344#", strReplacedPin);

}