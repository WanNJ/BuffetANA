import tensorflow as tf


import numpy as np
import LineDataSet


def getPredictResult(code,holdingDays):
    dataSet = LineDataSet.pre_deal_Line_Data(code,holdingDays)


    def weight_variable(shape):
        initial = tf.truncated_normal(shape, stddev = 0.001)
        return tf.Variable(initial)
    def bias_variable(shape):
        initial = tf.constant(0.001, shape=shape)
        return tf.Variable(initial)


    data = np.array(dataSet['data']).astype(np.float32)

    # print(len(data))

    labels = np.array(dataSet['target']).astype(np.float32)

    predict = np.array(dataSet['predict']).astype(np.float32)


    # print(data)


    train_x,train_y=[],[]

    for i in range(len(data)):
        train_y.append(labels[i])
        train_x.append(data[i])

    # print(train_x[0][0])
    # print(train_y)

    x = tf.placeholder("float", shape=[None, 10])
    y_ = tf.placeholder("float", shape=[None, 3])

    W = weight_variable(shape=[10,3])
    b = bias_variable(shape=[3])

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

    resultHead = ['up','down','smooth']

    ww =sess.run(W)
    bb =sess.run(b)
    yy = tf.nn.softmax(tf.matmul(predict,ww) + bb)

    resultArray =sess.run(yy)[0]

    acc = (accuracy.eval(feed_dict={
        x: data[-200:], y_: labels[-200:]}))

    result = {}

    js = 0;
    result['importance'] = dataSet['importance']
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