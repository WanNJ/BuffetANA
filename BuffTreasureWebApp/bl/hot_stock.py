import requests

url = 'http://stockhtm.finance.qq.com/sstock/quotpage/inc_hot/hot_all.js?0.5899452364532525'

res = requests.get(url)
res.encoding = 'GBK'
print(res.text.lstrip('var v_hot_all="').rstrip('|";\r\n'))
