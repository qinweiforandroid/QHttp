package com.qw.http.sample.domain;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qinwei on 2019-09-05 14:59
 * email: qinwei_it@163.com
 */
public class Profile implements Serializable {

    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywicmVzdGF1cmFudElkIjoxLCJyb2xlIjoiQlVTSU5FU1MiLCJ0aWQiOiJmOWVmMzEwOC01Y2Q2LTQxMmEtYTc4MS1jZTlmMWFhZmRhY2UiLCJlbmRwb2ludCI6ImFybjphd3M6c25zOnVzLXdlc3QtMjo4MzE3ODgwMTIyNTM6ZW5kcG9pbnQvQVBOU19TQU5EQk9YL2Zvb2RpZWF0cy1idXNpbmVzcy1kZXYvNmVjZjEyM2UtMThjNi0zMzZkLTliOTEtMDJiMzM1ZTE3MzM0IiwiaWF0IjoxNTY3NjUxMjYyLCJleHAiOjE1NjgyNTYwNjJ9.IRM0cNVtZGLt5PzmDXhoKItAStsp6m54WTN_5kBmhK0
     * sessionExpiration : 1570243262
     * tokenExpiration : 1568256062
     * user : {"id":3,"name":"user1567649309046","status":0,"email":"rest@foodieats.io","isEmailVerified":0,"phone":null,"countryCode":null,"facebookId":null,"googleId":null,"roles":[{"roleId":2,"roleName":"BUSINESS"},{"roleId":0,"roleName":"CONSUMER"}]}
     * restaurant : {"id":1,"name":"First","status":20,"logo":{"id":2,"url":"https://dnkejuwdn9feu.cloudfront.net/images/afd9444e-ccb5-4348-bc0d-58c8ee3e3d8d.png"},"background":{"id":3,"url":"https://dnkejuwdn9feu.cloudfront.net/images/ae15e965-9e49-4442-b29b-57e4288f95d1.png"},"timezone":-4,"longitude":121.5054671,"latitude":31.30970984,"email":"rest@foodieats.io","phone":"9676767645","countryCode":"1","rating":0,"ratedNumber":0,"isOpenEveryday":0,"isAllowOffline":1,"dinein":1,"pickup":1,"reservation":1,"taxRate":0,"address":"Public Toilet Yangpu","description":null,"cuisines":[{"id":1,"name":"Chinese"}],"openTimes":[]}
     */

    private String token;
    private int sessionExpiration;
    private int tokenExpiration;
    private UserBean user;
    private RestaurantBean restaurant;

    @NonNull
    @Override
    public String toString() {
        return user.name + ":" + user.email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSessionExpiration() {
        return sessionExpiration;
    }

    public void setSessionExpiration(int sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }

    public int getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(int tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public RestaurantBean getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantBean restaurant) {
        this.restaurant = restaurant;
    }

    public static class UserBean {
        /**
         * id : 3
         * name : user1567649309046
         * status : 0
         * email : rest@foodieats.io
         * isEmailVerified : 0
         * phone : null
         * countryCode : null
         * facebookId : null
         * googleId : null
         * roles : [{"roleId":2,"roleName":"BUSINESS"},{"roleId":0,"roleName":"CONSUMER"}]
         */

        private int id;
        private String name;
        private int status;
        private String email;
        private int isEmailVerified;
        private Object phone;
        private Object countryCode;
        private Object facebookId;
        private Object googleId;
        private List<RolesBean> roles;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getIsEmailVerified() {
            return isEmailVerified;
        }

        public void setIsEmailVerified(int isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(Object countryCode) {
            this.countryCode = countryCode;
        }

        public Object getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(Object facebookId) {
            this.facebookId = facebookId;
        }

        public Object getGoogleId() {
            return googleId;
        }

        public void setGoogleId(Object googleId) {
            this.googleId = googleId;
        }

        public List<RolesBean> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesBean> roles) {
            this.roles = roles;
        }

        public static class RolesBean {
            /**
             * roleId : 2
             * roleName : BUSINESS
             */

            private int roleId;
            private String roleName;

            public int getRoleId() {
                return roleId;
            }

            public void setRoleId(int roleId) {
                this.roleId = roleId;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }
        }
    }

    public static class RestaurantBean {
        /**
         * id : 1
         * name : First
         * status : 20
         * logo : {"id":2,"url":"https://dnkejuwdn9feu.cloudfront.net/images/afd9444e-ccb5-4348-bc0d-58c8ee3e3d8d.png"}
         * background : {"id":3,"url":"https://dnkejuwdn9feu.cloudfront.net/images/ae15e965-9e49-4442-b29b-57e4288f95d1.png"}
         * timezone : -4
         * longitude : 121.5054671
         * latitude : 31.30970984
         * email : rest@foodieats.io
         * phone : 9676767645
         * countryCode : 1
         * rating : 0
         * ratedNumber : 0
         * isOpenEveryday : 0
         * isAllowOffline : 1
         * dinein : 1
         * pickup : 1
         * reservation : 1
         * taxRate : 0
         * address : Public Toilet Yangpu
         * description : null
         * cuisines : [{"id":1,"name":"Chinese"}]
         * openTimes : []
         */

        private int id;
        private String name;
        private int status;
        private LogoBean logo;
        private BackgroundBean background;
        private int timezone;
        private double longitude;
        private double latitude;
        private String email;
        private String phone;
        private String countryCode;
        private int rating;
        private int ratedNumber;
        private int isOpenEveryday;
        private int isAllowOffline;
        private int dinein;
        private int pickup;
        private int reservation;
        private int taxRate;
        private String address;
        private Object description;
        private List<CuisinesBean> cuisines;
        private List<?> openTimes;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public LogoBean getLogo() {
            return logo;
        }

        public void setLogo(LogoBean logo) {
            this.logo = logo;
        }

        public BackgroundBean getBackground() {
            return background;
        }

        public void setBackground(BackgroundBean background) {
            this.background = background;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getRatedNumber() {
            return ratedNumber;
        }

        public void setRatedNumber(int ratedNumber) {
            this.ratedNumber = ratedNumber;
        }

        public int getIsOpenEveryday() {
            return isOpenEveryday;
        }

        public void setIsOpenEveryday(int isOpenEveryday) {
            this.isOpenEveryday = isOpenEveryday;
        }

        public int getIsAllowOffline() {
            return isAllowOffline;
        }

        public void setIsAllowOffline(int isAllowOffline) {
            this.isAllowOffline = isAllowOffline;
        }

        public int getDinein() {
            return dinein;
        }

        public void setDinein(int dinein) {
            this.dinein = dinein;
        }

        public int getPickup() {
            return pickup;
        }

        public void setPickup(int pickup) {
            this.pickup = pickup;
        }

        public int getReservation() {
            return reservation;
        }

        public void setReservation(int reservation) {
            this.reservation = reservation;
        }

        public int getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(int taxRate) {
            this.taxRate = taxRate;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public List<CuisinesBean> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<CuisinesBean> cuisines) {
            this.cuisines = cuisines;
        }

        public List<?> getOpenTimes() {
            return openTimes;
        }

        public void setOpenTimes(List<?> openTimes) {
            this.openTimes = openTimes;
        }

        public static class LogoBean {
            /**
             * id : 2
             * url : https://dnkejuwdn9feu.cloudfront.net/images/afd9444e-ccb5-4348-bc0d-58c8ee3e3d8d.png
             */

            private int id;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class BackgroundBean {
            /**
             * id : 3
             * url : https://dnkejuwdn9feu.cloudfront.net/images/ae15e965-9e49-4442-b29b-57e4288f95d1.png
             */

            private int id;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class CuisinesBean {
            /**
             * id : 1
             * name : Chinese
             */
            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
