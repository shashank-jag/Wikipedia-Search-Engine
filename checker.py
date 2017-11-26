f = open("./Freq/Segmented_Freq5.txt",'r')
str = f.read()
i = 0
j = 9577586
# while str[i] != '\n' :
# 	i-=1
i+=1

s = "13735358-"
while True:
	if str[i:i+len(s)] == s:
		break
	i+=1

	print(i)
# System.out.println("./Freq/Segmented_Freq"+sec_entry[1]+".txt"+Integer.parseInt(sec_entry[3])+" "+Integer.parseInt(sec_entry[2])+"***"+word);