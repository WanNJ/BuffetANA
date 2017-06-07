# encode:utf-8

import requests
import io
from bs4 import BeautifulSoup
import sys
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf-8')

url = 'http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpInfo/stockid/' + sys.argv[1] + '.phtml'
res = requests.get(url)
res.encoding = 'gbk'
soup = BeautifulSoup(res.text, "html.parser")

companyInfo = soup.select('#comInfo1')[0].select('tr')


company = {
    "公司名称": companyInfo[0].select('td')[1].text,
    "公司英文名称": companyInfo[1].select('td')[1].text,
    "上市市场": companyInfo[2].select('td')[1].text,
    "上市日期": companyInfo[2].select('td')[3].text.strip(),
    "发行价格": float(companyInfo[3].select('td')[1].text),
    "成立日期": companyInfo[4].select('td')[1].select('a')[0].text,
    "注册资本": companyInfo[4].select('td')[3].text,
    "机构类型": companyInfo[5].select('td')[1].text,
    "组织形式": companyInfo[5].select('td')[3].text,
    "董事会秘书": companyInfo[6].select('td')[1].text,
    "公司电话": companyInfo[6].select('td')[3].text,
    "董秘电话": companyInfo[8].select('td')[1].text,
    "公司传真": companyInfo[8].select('td')[3].text,
    "董秘传真": companyInfo[10].select('td')[1].text,
    "公司电子邮箱": companyInfo[10].select('td')[3].select('a')[0].text,
    "董秘电子邮箱": companyInfo[12].select('td')[1].text,
    "公司网址": companyInfo[12].select('td')[3].text,
    "邮政编码": companyInfo[14].select('td')[1].text,
    "证券简称更名历史": companyInfo[16].select('td')[1].text,
    "注册地址": companyInfo[17].select('td')[1].text,
    "办公地址": companyInfo[18].select('td')[1].text,
    "公司简介": companyInfo[19].select('td')[1].text.replace('"', "\\'"),
    "经营范围": companyInfo[20].select('td')[1].text
}

if len(companyInfo[3].select('td')[3].select('a')) == 0:
    company["主承销商"] = ''
else:
    company["主承销商"] = companyInfo[3].select('td')[3].select('a')[0].text
if len(companyInfo[14].select('td')[3].select('a')) == 0:
    company["信息披露网址"] = ''
else:
    company["信息披露网址"] = companyInfo[14].select('td')[3].select('a')[0].text
print(company)
