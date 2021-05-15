package at.termftp.backend.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FtpType {
    FTP, SFTP, FTPS;

    public static List<String> getTypesAsList(){
        return Arrays.stream(FtpType.values()).map(Enum::toString).collect(Collectors.toList());
    }
}
