import pandas as pd
competences = pd.read_csv('train.csv')
X = competences.drop(columns=['Dest', 'Dest1'])
y1 = competences['Dest1']
y = competences['Dest']
events = list(set(competences['Event'].to_numpy()))
from sklearn.model_selection import train_test_split
train_x, test_x, train_y, test_y = train_test_split(X, y1, test_size=0.333, random_state=42)
from sklearn.neural_network import MLPClassifier
nn = MLPClassifier().fit(train_x, train_y)
print(nn.predict([[1,1,1]]))
print(nn.score(test_x, test_y))
f = open('text.txt', 'w')
train_x, test_x, train_y, test_y = train_test_split(X, y, test_size=0.333, random_state=42)
from sklearn.tree import DecisionTreeClassifier
m = DecisionTreeClassifier(max_depth= 5, criterion = 'entropy')
m.fit(train_x, train_y)
from sklearn.metrics import accuracy_score
print(accuracy_score(test_y, m.predict(test_x)))
for i in events:
    for j in range (1, 21):
        for k in range (1, 6):
            d = nn.predict([[i, j, k]])
            if (d[0] == 0):
                f.write(str(i) + ' ' + str(j) + ' ' + str(k) + ' ' + str(k) + '\n')
            else:
                d = m.predict([[i,j,k]])
                f.write(str(i) + ' ' + str(j) + ' ' + str(k) + ' ' + str(d[0]) + '\n')
f.close()