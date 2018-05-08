fileName = uigetfile('*.mat');
FileData = load(fileName);
%csvwrite('FileName.csv', FileData.M);
%TODO
