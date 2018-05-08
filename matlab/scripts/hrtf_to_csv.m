% If first time run, create new csv directory
% Requires CIPIC env to be set to the cipic standard_hrir_database directory
cipic = getenv('CIPIC');
cipicCsv = strcat(cipic, '/csv/');
mkdir(cipic)

while true
        try
            [fileName, path] = uigetfile(cipic,'*.mat');
            hrtf = load(strcat(path,fileName));
        catch ME
            fclose('all');
            break  
        end
        
        %Create respective directory
        subjectDir = strcat(cipic, hrtf.name);
        mkdir(subjectDir);
        
        %Write all variables from struct
        csvwrite(strcat(subjectDir,'/on_r.csv'), hrtf.OnR);
        csvwrite(strcat(subjectDir,'/on_l.csv'), hrtf.OnL);
        csvwrite(strcat(subjectDir,'/itd.csv'), hrtf.ITD);
        csvwrite(strcat(subjectDir,'/hrir_r.csv'), hrtf.hrir_r);
        csvwrite(strcat(subjectDir,'/hrir_l.csv'), hrtf.hrir_l);
end