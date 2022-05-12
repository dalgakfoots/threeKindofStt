package onthelive.threeKindofStt.entity;

import lombok.Data;
import onthelive.threeKindofStt.entity.enums.SttType;

@Data
public class SttValue {

    private FileInfo audioFile;
    private SttType stt_source;

}
