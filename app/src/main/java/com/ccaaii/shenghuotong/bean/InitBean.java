package com.ccaaii.shenghuotong.bean;


import java.util.List;

public class InitBean {
    public static final String H5PAGE="h5Page";
    public static final String ANDROIDAPKURL="androidApkUrl";
    public static final String TOUZHULINK="touzhulink";


    public String h5Page;
    public String androidApkUrl;
    public String touzhulink;

    private List<LotteryTypesBean> lotteryTypes;
    private List<SlidePicturesBean> slidePictures;

    public String getH5Page() {
        return h5Page;
    }

    public void setH5Page(String h5Page) {
        h5Page = h5Page;
    }

    public List<LotteryTypesBean> getLotteryTypes() {
        return lotteryTypes;
    }

    public void setLotteryTypes(List<LotteryTypesBean> lotteryTypes) {
        this.lotteryTypes = lotteryTypes;
    }

    public List<SlidePicturesBean> getSlidePictures() {
        return slidePictures;
    }

    public void setSlidePictures(List<SlidePicturesBean> slidePictures) {
        this.slidePictures = slidePictures;
    }

    public static class LotteryTypesBean {
        /**
         * description :
         * name : lhc
         * url : http://www.baidu.com
         */

        private String description;
        private String name;
        private String url;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SlidePicturesBean {
        /**
         * id : 1
         * image : http://a1.att.hudong.com/15/49/300000928390128006492037132_950.jpg
         * target : http://www.baidu.com
         * type : SLIDE
         */

        private int id;
        private String image;
        private String target;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
