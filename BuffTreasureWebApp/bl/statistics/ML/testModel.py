import tensorflow as tf


import numpy as np
import pandas as pd
import getDataSet


def getPredictResult(code,holdingDays):
    getDataSet.pre_deal_data('000001',holdingDays)


    def weight_variable(shape):
        initial = tf.truncated_normal(shape, stddev = 0.001)
        return tf.Variable(initial)
    def bias_variable(shape):
        initial = tf.constant(0.001, shape=shape)
        return tf.Variable(initial)

    f=open('./dataset.csv')
    l=open('./labelset.csv')
    p=open('./predict.csv')

    df=pd.read_csv(f)
    # print(df.head())
    data = np.array(df)
    dl = pd.read_csv(l)

    labels = np.array(dl)
    dp = pd.read_csv(p)
    predict = np.array(dp).astype(np.float32)





    train_x,train_y=[],[]

    for i in range(len(data)):
        train_y.append(labels[i])
        train_x.append(data[i])

    # print(train_x[0][0])
    # print(train_y)

    x = tf.placeholder("float", shape=[None, 9*holdingDays])
    y_ = tf.placeholder("float", shape=[None, 6])

    W = weight_variable(shape=[9*holdingDays,6])
    b = bias_variable(shape=[6])

    sess = tf.InteractiveSession()
    sess.run(tf.global_variables_initializer())

    y = tf.nn.softmax(tf.matmul(x,W) + b)

    # cross_entropy = -tf.reduce_sum(y_*tf.log(y))
    loss = tf.reduce_mean(tf.square(y - y_))
    cross_entropy = -tf.reduce_sum(y_*tf.log(tf.clip_by_value(y,1e-10,1.0)))

    train_step = tf.train.GradientDescentOptimizer(0.0001).minimize(cross_entropy)

    #print(train_x)
    correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))

    accuracy = tf.reduce_mean(tf.cast(correct_prediction, "float"))


    for i in range(2000):
        sess.run(train_step,feed_dict={x: data[:-200], y_: labels[:-200]})

    resultHead = dl.head(0)

    ww =sess.run(W)
    bb =sess.run(b)
    yy = tf.nn.softmax(tf.matmul(predict,ww) + bb)

    resultArray =sess.run(yy)[0]

    acc = (accuracy.eval(feed_dict={
        x: data[-200:], y_: labels[-200:]}))

    result = {}

    js = 0;

    result["accuracy"] = str(acc)
    for st in resultHead:
        result[st] = str(resultArray[js])
        js += 1
    return result



import sys
code = sys.argv[1]
holdingPerioud = sys.argv[2]

out = [getPredictResult(code,int(holdingPerioud))]


import json
print(json.dumps(out))