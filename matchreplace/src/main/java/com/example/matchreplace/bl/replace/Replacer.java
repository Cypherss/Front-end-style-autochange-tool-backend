package com.example.matchreplace.bl.replace;


import com.example.matchreplace.dto.TreeNodeDTO;
import com.example.matchreplace.vo.TreeNode;
import org.springframework.stereotype.Service;

@Service
public interface Replacer {
    public String replaceAndSave(String fileId, TreeNode body);
    public TreeNodeDTO replace(TreeNode body);

}
