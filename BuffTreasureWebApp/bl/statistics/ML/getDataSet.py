import datetime
import pymongo
import numpy as np
import csv


def connect_mongodb():
    servers = "mongodb://localhost:27017"
    conn = pymongo.MongoClient(servers)
    db = conn.allInfo
    return db

def pre_deal_data(code , holdingPeriods):
    pro = ['afterAdjClose', 'date', 'turnOverRate', 'changePrice', 'volume', 'close', 'low', 'high',
            'changeRate', 'beforeAdjClose']
    propre = ['afterAdjClose', 'turnOverRate', 'changePrice', 'volume', 'close', 'low', 'high',
             'changeRate', 'beforeAdjClose']

    heads = []

    label0 = {'less10':1,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label1 = {'less10':0,'10-7.5':1,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label2 = {'less10':0,'10-7.5':0,'7.5-5':1,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label3 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':1,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label4 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':1,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label5 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':1,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label6 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':1,'5.5-7.5':0,'7.5-10':0,'more10':0}
    label7 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':1,'7.5-10':0,'more10':0}
    label8 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':1,'more10':0}
    label9 = {'less10':0,'10-7.5':0,'7.5-5':0,'5-2.5':0,'2.5-0':0,'0-2.5':0,'2.5-5.5':0,'5.5-7.5':0,'7.5-10':0,'more10':1}

    labelhead = list(label0.keys())
    labels = [label0,label1,label2,label3,label4,label5,label6,label7,label8,label9]
    labelset = []

    for i in range(holdingPeriods):
        for st in propre:
            heads.append(st+str(i))

    db = connect_mongodb()
    collection = db[code]
    beginDate = datetime.datetime.strptime('2005-04-05', '%Y-%m-%d')
    cc = collection.find({'volume': {'$gt': 0}, 'date': {'$gt': beginDate}}, pro).sort('date',pymongo.ASCENDING)

    nor = {}

    data = []
    for o in cc:
        o.pop('_id')
        data.append(o)

    js = 0
    temp = data[0]
    for key in list(temp.keys()):
        nor[key] =[]
    for o in data:
        for key in list(o.keys()):
            nor[key].append(o[key])
        js += 1

    for o in data:
        for key in list(o.keys()):
            if key!='date':
                o[key] = (o[key]-np.mean(nor[key]))/np.std(nor[key])

    with open('dataset.csv', 'w', encoding='gbk') as f1:
        fieldnames = heads
        writer = csv.DictWriter(f1, fieldnames=fieldnames)
        writer.writeheader()
        content = {}
        for i in range(holdingPeriods,len(data) - holdingPeriods):
            change = 100*(data[i+holdingPeriods-1]['close']-data[i]['close'])/data[i]['close']
            ll = int((change+10)/2.5)+1
            if ll < 0:
                ll = 0
            if ll > 9:
                ll = 9
            #print(ll)
            label = labels[ll]
            labelset.append(label)
            proind = 0
            for ind in range(i,i-holdingPeriods,-1):
                value = data[ind]
                for js in range(len(propre)):
                    head = heads[proind]
                    try:
                        content[head] = value[head[:-1]]
                    except :
                        content[head] = value[head[:-2]]

                    proind +=1
            writer.writerow(content)

    with open('labelset.csv', 'w', encoding='gbk') as f1:
        fieldnames = labelhead
        writer = csv.DictWriter(f1, fieldnames=fieldnames)
        writer.writeheader()
        for l in labelset:
            writer.writerow(l)

    with open('predict.csv', 'w', encoding='gbk') as f1:
        l = len(data)
        fieldnames = heads
        writer = csv.DictWriter(f1, fieldnames=fieldnames)
        writer.writeheader()
        proind = 0
        for ind in range(l-1, l - holdingPeriods, -1):
            value = data[ind]
            for js in range(len(propre)):
                head = heads[proind]
                try:
                    content[head] = value[head[:-1]]
                except:
                    content[head] = value[head[:-2]]
                proind += 1
        writer.writerow(content)