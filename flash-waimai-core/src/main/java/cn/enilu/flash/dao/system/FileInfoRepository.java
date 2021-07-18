package cn.enilu.flash.dao.system;

import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.dao.BaseRepository;

import java.util.List;

public interface FileInfoRepository  extends BaseRepository<FileInfo,Long> {
    FileInfo findByRealFileName(String fileName);
    List<FileInfo> findAllByOriginalFileName(String fileName);
}
