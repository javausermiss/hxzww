package com.fh.alipay;

/**
 * 支付宝基本配置
 */
public class AlipayConfig {

        // 1.商户appid
        public static String APPID = "2017111609966777";

        // 2.私钥 pkcs8格式的
        public static String RSA_PRIVATE_KEY ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQ4eSB9j6rz6PEn/YoxZFOAuLbAVlClljRtb3Mne3mCgqhz0kyxJTF4iLC92hFzrGcZoa0t2XsVVhpcyuebNunqrnxhIuN+8YoP+g2CBFNYg9oPjhns4eIi9RK+6X7iAcevSlloHCIDtiB/pZaU0Y0/6FfMfTAhf+VFjKtBin+5cskp2YkwbwUzJ1MheTJNx40PH/46oqEt6yxqXOlmQIMsI4fFf1bIy5z8DmpzXcRyDvzMDs6RfegfDBLFPVQ1IWUJAiAErYfwxd8Ce6bobIPGz+dFPTfExOCxmUwwBxTIMaOFB90SactFKoPBXIAo12pD3bsLxAATpj6KmYKig71AgMBAAECggEAbk/AN3+xCgoHrE8LEtISp24lIhDt0Hg+2jVhdOkOWKdunvL/zarFh8F9LCtJ9lTBAM9DY1d8QCxfK7LoFDDYMdoYx55ez1VS9BRsjq8n1V6DUqCFO9gCquGkWw8tus1AdpQab+lvpPiAusGH4vAitXmpeZzEuM2N4wrKvNjDovJWTmd3YRaZTEUJUKb7X+PWlZ/E/CU0c4HjkVF51P4zHSn88bl6EKdiOODEQSY4CuX489+vXtwv+95LUB6OfL5e3CDAw5eTP10Gm7CBbGK76gOVwvRUjljMhCf6QBT8uh7o7Fv3AnM0kWtcn7c48ox+kmZmQpMjpM+GHXm0aoxiQQKBgQDao371ftX9X+ldpkv4lrwfO8/3RCJfcQEOLP0E1nrc+uuLxuKrPG66Fe2BhkC5q+4VvzqDjWsRbxn6MGuhAeS7zJKucsfLJYyc3bxpVCarO2VcybUhxoN5LSil4D4LebZ1XokZb0jr6ix3U1oyxqg19YbWmRp/eGZYLccKLAIIhQKBgQCpo+BVeZGp/vk6r0pDQUZ7+XavEkwVDcfpzNZHy50RxwJnVlNi+kbqs+3Kt2JGW4EOXOrZoVrlvbvKrBZfQUfGpcFrL4FbY5wOIMcS4Lf1XVHVuE+pTO9cnxx7CL+bAmik9+Ji9tY58TTTNzA3q67cX2qZmUg8iW4azDwxuNbvsQKBgHPEMDwy3YGcoEdK/1zwC0oKdJJ7jCu2/RURcyN1oXaSkdfgBjMe+44igDCd8nWdeKIhMA1p+vAvQLT9oG+PPhrGNkCSXAHPrwV8TwACY7s05vXbg8IPh5vI8oXqF1AiMR8yYCuMhjMMOmEwTEkocPapO60zQhmOmbpE+ugC50y1AoGBAJWPEDHraxvX2nz9m9htjSmxxwwUcpVBQBYFbImBnunUbW8TsZKamrlDhorpYLAs7jvueXMDibLwssa9L0Atuo0NB9hq2n3q9OOTzE79ZdHhw0tMIpddZ85dzOs/zEVqOz3t+V5MxpPw7ySkDFOYfiJ10PK4K/KLK1Je61be4kgRAoGAOg77qd702tprpLv2FbRmp1HKSBqMTwP39wA5sHgWeNFtHsntYjmAvjTZ9H+s0Pxt0ochzjBr9t+IAn4hmuURsCzAe/FfQbHqRMmLsxON4Nby+JaGfSW65ZggiMZ9NfRL/NALLJ0Rce9aqu7Q6sXeM5hE8zEPzGdGQRbr01bySUE=";

        // 3.支付宝公钥
        public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgWCgo16rRtZiWug+FAazvJ8zFkcGXCORrv3xWqliDm308I0rwtRNFCXGmVpnMuewGJWivW46bS6asZrV7lxYrFETVSNyuaRi1UAzzzl/YM3GBdFit2iel91U4FIvBfqnV4iTiT0JjqjXkXaXnah6JPJLHlRgXikc0vJcq6QYJikw8W8IlbVOd64PiFzjRLN2PqmBsOknfd8JsgVBgpioJp39deSaXrjKKkck5fkVCHWuU1l4p7NJTz0g32N6tEYuw18taAEoizFjhc8yRySuAFr5pPiycxFyqILIkQ09ytETitCt4gJdnZHIWyXsxubnw6cv2nc7wNnrqViqmKzJWQIDAQAB";

        // 4.服务器异步通知页面路径
        public static String notify_url = "http://111.231.139.61:18081/pooh-web/app/pay/AlipayCallBack";

        // 6.请求网关地址
        public static String URL = "https://openapi.alipay.com/gateway.do";

        // 7.编码
        public static String CHARSET = "UTF-8";

        // 8.返回格式
        public static String FORMAT = "json";

        // 9.加密类型
        public static String SIGNTYPE = "RSA2";


        public static String SELLER_ID= "2088002448041012";


}