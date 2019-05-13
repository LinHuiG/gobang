import keras
from keras import models
from keras import layers
import numpy as np
import os
from keras import backend as K
K.set_image_dim_ordering('th')

model_dir='model.h5'
def creatmodel():
    mo = models.Sequential()
    mo.add(layers.Conv2D(filters=3,kernel_size=[9,9],activation='relu',padding='same', input_shape=[1,19,19,]))
    mo.add(layers.Conv2D(filters=3,kernel_size=[1,1], activation='tanh'))
    mo.add(layers.Conv2D(filters=1,kernel_size=[1,1], activation='relu'))
    mo.add(layers.Conv2D(filters=1,kernel_size=[1,1], activation='relu'))
    mo.compile(optimizer='rmsprop', loss='categorical_crossentropy', metrics=['accuracy'])
    print(mo.summary())
    return  mo;
def getmodel():
    if(os.path.exists(model_dir)):
        mo=models.load_model(model_dir)
        return mo
    mo=creatmodel()
    return mo
def win(qp):
    for i in range(0,18):
        c=0
        for j in range(1,18):
            if(qp[i][j]==qp[i][j-1])and(qp[i][j]!=-1):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[i][j]
        c=0
        for j in range(1,18):
            if(qp[j][i]==qp[j-1][i])and(qp[j][i]!=-1):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[j][i]
    for i in range(0,37):
        x=i+1
        y=1
        if(i>=19):
            x=1
            y=i-19+1
        c=0
        while(x>=0 and y>=0 and x<19 and y<19):
            if(qp[x][y]==qp[x-1][y-1]):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[x][y]
            x+=1
            y+=1
        x = i -1
        y = 1
        if (i >= 19):
            x = 18
            y = i - 19 - 1
        c = 0
        while (x >= 0  and  y >= 0 and x < 19  and  y < 19):
            if (qp[x][y] == qp[x + 1][y - 1]):
                c += 1
            else:
                c = 0
            if (c == 5):
                return qp[x][y]
            x -= 1
            y += 1
    return -1
def xzqp(qp):
    an=np.zeros((19, 19))
    for i in range(0,18):
        for j in range(0,18):
            an[i][j]=qp[j][18-i]
    return an
def trainsj(mo,qplist,lzlist,winer):
    qplist2=[]
    if (winer == 0):
        for qp in qplist:
            x=qpzh(qp)
            qplist2.append(x)
    qplistfn=[]
    lzlistfn=[]
    for qp in qplist2:
        an=qp
        for k in range(1,4):
            qplistfn.append([an])
            an=xzqp(an)
    for lz in lzlist:
        bn=lz
        for k in range(1, 4):
            lzlistfn.append([bn])
            bn=xzqp(bn)
    sr=np.asarray(qplistfn)
    sc=np.asarray(lzlistfn)
    mo.fit(sr,sc,epochs=50,batch_size=10)
    mo.save(model_dir)


def qpzh(qp):
    for i in range(0, 18):
        for j in range(0, 18):
            if (qp[i][j] > 0):
                qp[i][j] += 1
                qp[i][j] %= 2
    return qp

def trainmodel(mo,cs):
    while(cs>0):
        cs-=1
        heizi = []
        heizil = []
        baizi = []
        baizil = []
        qp = np.zeros((19, 19))
        count = 0
        for i in range(0, 18):
            for j in range(0, 18):
                qp[i][j] = -1
        while (win(qp) == -1):
            count += 1

            an=qp
            if(count%2==0):
                an=qpzh(qp)
            [maxi,maxj,ans]=getAns(an,mo,1)

            if (count % 2 == 1):
                heizi.append(qp)
                heizil.append(ans)
                qp[maxi][maxj]=1
            else:
                baizi.append(qp)
                baizil.append(ans)
                qp[maxi][maxj] = 0

        if(win(qp)==1):
            trainsj(mo,heizi,heizil,1)
        else:
            trainsj(mo, baizi, baizil, 0)
def getAns(qp,mo=-1,sc=-1):
    if(mo==-1):
        mo=getmodel()
    ans=mo.predict(np.asarray([[qp]]))
    ans=ans.reshape(19,19)
    maxi = 0
    maxj = 0
    for i in range(0, 18):
        for j in range(0, 18):
            if ((ans[i][j] > ans[maxi][maxj]) or qp[maxi][maxj] != -1) and qp[i][j] == -1:
                maxi = i
                maxj = j
    if(sc==-1):
        return [maxi,maxj]
    return [maxi,maxj,ans]

if __name__ == '__main__':
    mymodel=getmodel()
    trainmodel(mymodel,2)

