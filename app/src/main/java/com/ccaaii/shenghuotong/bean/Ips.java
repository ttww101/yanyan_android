package com.ccaaii.shenghuotong.bean;

/**
 */

public class Ips {


    /**
     * success : 1
     * result : {"status":"OK","ip":"8.8.8.8","ip_str":"8.8.8.1","ip_end":"8.8.8.254","inet_str":"134744065","inet_end":"134744318","operators":"未知","att":"美国","detailed":"美国","area_style_simcall":"美国","area_style_areanm":"美利坚合众国"}
     */

    private String success;
    private ResultBean result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * status : OK
         * ip : 8.8.8.8
         * ip_str : 8.8.8.1
         * ip_end : 8.8.8.254
         * inet_str : 134744065
         * inet_end : 134744318
         * operators : 未知
         * att : 美国
         * detailed : 美国
         * area_style_simcall : 美国
         * area_style_areanm : 美利坚合众国
         */

        private String status;
        private String ip;
        private String ip_str;
        private String ip_end;
        private String inet_str;
        private String inet_end;
        private String operators;
        private String att;
        private String detailed;
        private String area_style_simcall;
        private String area_style_areanm;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getIp_str() {
            return ip_str;
        }

        public void setIp_str(String ip_str) {
            this.ip_str = ip_str;
        }

        public String getIp_end() {
            return ip_end;
        }

        public void setIp_end(String ip_end) {
            this.ip_end = ip_end;
        }

        public String getInet_str() {
            return inet_str;
        }

        public void setInet_str(String inet_str) {
            this.inet_str = inet_str;
        }

        public String getInet_end() {
            return inet_end;
        }

        public void setInet_end(String inet_end) {
            this.inet_end = inet_end;
        }

        public String getOperators() {
            return operators;
        }

        public void setOperators(String operators) {
            this.operators = operators;
        }

        public String getAtt() {
            return att;
        }

        public void setAtt(String att) {
            this.att = att;
        }

        public String getDetailed() {
            return detailed;
        }

        public void setDetailed(String detailed) {
            this.detailed = detailed;
        }

        public String getArea_style_simcall() {
            return area_style_simcall;
        }

        public void setArea_style_simcall(String area_style_simcall) {
            this.area_style_simcall = area_style_simcall;
        }

        public String getArea_style_areanm() {
            return area_style_areanm;
        }

        public void setArea_style_areanm(String area_style_areanm) {
            this.area_style_areanm = area_style_areanm;
        }
    }
}
