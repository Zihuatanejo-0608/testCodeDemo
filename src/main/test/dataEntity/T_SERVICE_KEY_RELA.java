package dataEntity;

import java.util.Date;

/**
 * Created by andy on 2019/3/1.
 *
 */
public class T_SERVICE_KEY_RELA {
    private Integer id;
    private Date created_at;
    private String created_by;
    private Date updated_at;
    private String updated_by;
    private String key;
    private String inst_interface_type;
    private String inst_interface_status;
    private String data_source_type;
    private String interface_decr;
    private String interface_visit_item;
    private String interface_product_manager;
    private String interface_programmer;
    private String interface_data_analyst;
    private String table_name;
    private String strategy_type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInst_interface_type() {
        return inst_interface_type;
    }

    public void setInst_interface_type(String inst_interface_type) {
        this.inst_interface_type = inst_interface_type;
    }

    public String getInst_interface_status() {
        return inst_interface_status;
    }

    public void setInst_interface_status(String inst_interface_status) {
        this.inst_interface_status = inst_interface_status;
    }

    public String getData_source_type() {
        return data_source_type;
    }

    public void setData_source_type(String data_source_type) {
        this.data_source_type = data_source_type;
    }

    public String getInterface_decr() {
        return interface_decr;
    }

    public void setInterface_decr(String interface_decr) {
        this.interface_decr = interface_decr;
    }

    public String getInterface_visit_item() {
        return interface_visit_item;
    }

    public void setInterface_visit_item(String interface_visit_item) {
        this.interface_visit_item = interface_visit_item;
    }

    public String getInterface_product_manager() {
        return interface_product_manager;
    }

    public void setInterface_product_manager(String interface_product_manager) {
        this.interface_product_manager = interface_product_manager;
    }

    public String getInterface_programmer() {
        return interface_programmer;
    }

    public void setInterface_programmer(String interface_programmer) {
        this.interface_programmer = interface_programmer;
    }

    public String getInterface_data_analyst() {
        return interface_data_analyst;
    }

    public void setInterface_data_analyst(String interface_data_analyst) {
        this.interface_data_analyst = interface_data_analyst;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getStrategy_type() {
        return strategy_type;
    }

    public void setStrategy_type(String strategy_type) {
        this.strategy_type = strategy_type;
    }
}
