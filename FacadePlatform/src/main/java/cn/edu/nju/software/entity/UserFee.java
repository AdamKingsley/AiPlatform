package cn.edu.nju.software.entity;

import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "user_fee_fund")
@NameStyle(Style.normal)
public class UserFee {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String fundGuid;

    private Integer feeTypeId;

    private BigDecimal fund;

    private Integer userId;

    private Integer tradeCode;

    private Date addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFundGuid() {
        return fundGuid;
    }

    public void setFundGuid(String fundGuid) {
        this.fundGuid = fundGuid == null ? null : fundGuid.trim();
    }

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(Integer tradeCode) {
        this.tradeCode = tradeCode;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}