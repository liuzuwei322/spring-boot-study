package com.baidu.springbootstudy.entity;

import javax.persistence.*;

@Entity
@Table(name = "label_info")
public class LabelInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nid;

    @Column(length = 128, name = "label_name")
    String labelName;

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return "LabelInfo{" +
                "nid=" + nid +
                ", labelName='" + labelName + '\'' +
                '}';
    }
}
