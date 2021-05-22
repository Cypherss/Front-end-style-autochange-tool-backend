package com.example.matchreplace.blImpl.replace;


import com.example.matchreplace.Utils.ObjectStorageUtils;
import com.example.matchreplace.bl.replace.Replacer;
import com.example.matchreplace.dto.InternalNodeDTO;
import com.example.matchreplace.dto.LeafNodeDTO;
import com.example.matchreplace.dto.TreeNodeDTO;
import com.example.matchreplace.vo.InternalNode;
import com.example.matchreplace.vo.LeafNode;
import com.example.matchreplace.vo.TreeNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplacerImpl implements Replacer {

    @Autowired
    ObjectStorageUtils objectStorageUtils;

    public String replaceAndSave(TreeNode body){
        TreeNodeDTO treeNodeDTO = replace(body);
        String json = ObjectToJson(treeNodeDTO);
        String saveId = objectStorageUtils.saveFile(json);
        return saveId;
    }

    @Override
    public TreeNodeDTO replace(TreeNode body) {
        if(body.getClass() == InternalNode.class){
            InternalNodeDTO result = new InternalNodeDTO(body.getInfo());
            if(body.getMatcher()!=null)
                result.getInfo().setUsedCss(body.getMatcher().getInfo().getUsedCss());
            List<TreeNode> bodyChildren = ((InternalNode)body).getChildren();
            TreeNodeDTO[] children = new TreeNodeDTO[bodyChildren.size()];
            for(int i =0; i<bodyChildren.size();i++){
                children[i] = replace(((InternalNode) body).getChildren().get(i));
            }
            result.setChildren(children);
            return result;
        }else{
            LeafNodeDTO result = new LeafNodeDTO((LeafNode) body);
            if(body.getMatcher()!=null)
                result.getInfo().setUsedCss(body.getMatcher().getInfo().getUsedCss());
            return result;
        }
    }
    private String ObjectToJson(TreeNodeDTO body){
        Gson gson = new Gson();
        String json = gson.toJson(body);
        return json;
    }
}
