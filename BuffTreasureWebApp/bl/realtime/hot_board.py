# encode:utf-8
from functools import reduce
import io
import sys
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='utf-8')

import requests

url = 'http://stock.gtimg.cn/data/view/bdrank.php?&t=01/averatio&p=1&o=0&l=40&v=list_data'
res = requests.get(url)
res.encoding = 'gbk'
index = res.text.find("data:'")

boards = reduce(lambda x, y: 'bkhz' + x[4:] + ',' + 'bkhz' + y[4:], res.text[index + 6:].split(',')[0:5])
query = 'http://qt.gtimg.cn/q=' + boards + '&r=47686353'


res = requests.get(query)
res.encoding = 'gbk'

print(';'.join(list(map(lambda x: x.split('~')[1] + ',' + x.split('~')[6] + ',' + x.split('~')[8], res.text.strip('\n').split(';')[0:-1]))).strip('\n'))


