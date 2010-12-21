Проект по адаптации WMXJ для Google App Engine.

На данный момент работа с ключами Light не поддерживается (только WM Classic).

Использовать так:

WmService service = new WmService();

byte[] modulus = new byte[]{(byte)0x55, (byte)0x1F, (byte)0xFF, (byte)0x9E, (byte)0xC2, (byte)0xFE, (byte)0xF3, (byte)0x4C, (byte)0xD0, (byte)0x02, (byte)0x67, (byte)0xBE, (byte)0xB9, (byte)0xD7, (byte)0xCF, (byte)0x6A, (byte)0x72, (byte)0x63, (byte)0x66, (byte)0xE4, (byte)0xC9, (byte)0x5D, (byte)0xB5, (byte)0x62, (byte)0x77, (byte)0x44, (byte)0x3C, (byte)0x42, (byte)0x7B, (byte)0x65, (byte)0x9D, (byte)0xF0, (byte)0x1E, (byte)0x76, (byte)0xBE, (byte)0x7B, (byte)0x4D, (byte)0x99, (byte)0xC5, (byte)0x8C, (byte)0xA4, (byte)0xA6, (byte)0x09, (byte)0x62, (byte)0x6C, (byte)0xA4, (byte)0x91, (byte)0x94, (byte)0xEE, (byte)0xC2, (byte)0xEC, (byte)0xDD, (byte)0x53, (byte)0xF5, (byte)0x73, (byte)0x46, (byte)0xEC, (byte)0xD6, (byte)0xE1, (byte)0xB0, (byte)0xD4, (byte)0xCB, (byte)0xD6, (byte)0xF0, (byte)0x6E, (byte)0x05};
byte[] d = new byte[] {(byte)0x7D, (byte)0xB2, (byte)0x36, (byte)0x08, (byte)0xBD, (byte)0x9E, (byte)0x8C, (byte)0x0F, (byte)0xA8, (byte)0xED, (byte)0xAE, (byte)0x02, (byte)0x6C, (byte)0x29, (byte)0x59, (byte)0x1B, (byte)0x22, (byte)0xB0, (byte)0xCF, (byte)0x37, (byte)0xC2, (byte)0xFE, (byte)0xA7, (byte)0x13, (byte)0xB1, (byte)0x29, (byte)0xBF, (byte)0x0B, (byte)0x88, (byte)0x57, (byte)0xC0, (byte)0x59, (byte)0x1B, (byte)0xBD, (byte)0xA4, (byte)0xE9, (byte)0xB3, (byte)0x1D, (byte)0x2E, (byte)0xCC, (byte)0x5F, (byte)0xA0, (byte)0x8F, (byte)0xE0, (byte)0x9D, (byte)0xE6, (byte)0xA1, (byte)0xE5, (byte)0x0C, (byte)0x6C, (byte)0xEE, (byte)0x9B, (byte)0xD2, (byte)0x67, (byte)0xE3, (byte)0x90, (byte)0x10, (byte)0xCE, (byte)0xBD, (byte)0x58, (byte)0xFE, (byte)0xAA, (byte)0x28, (byte)0xBB, (byte)0xBF, (byte)0x00};

service.initWmSigner(new Wmid("933269435359"), d, modulus);

try {
	X6Response x6resp = service.x6("854579556367", "Message Subj", "Message body...");
	resp.getWriter().println(x6resp.getRetVal());
	
} catch (IOException e) {
	resp.getWriter().println(e);
}


, где modulus и d -- модуль и приватная экспонента, полученные из файла ключей kwm с помощью программы Key Extractor: http://wiki.webmoney.ru/wiki/show/Key+Extractor


ПРЕДУПРЕЖДЕНИЕ!

На данный момент в GAE нет возможности проверить удаленный сертификат сервера. В теории возможна подмена сервера WMT сервером злоумышленника, хотя практически сделать это очень сложно.

История изменений:

1. Изменено пространство имен на ru.webmoney (для совместимости).
2. Переделан класс HttpRequester: удалена работа с сокетами и добавлена работа с сервером по протоколу HTTP/HTTPS с помощью Google Url Fetch.
3. Удален пакет WMSigner lv.flancer.wmt.xml.wmsigner и заменен на wmsignerjx см. http://wiki.webmoney.ru/wiki/show/WMSignerJX
4. Начата переработка обработки исключений (для X6). Ее нужно полностью пересматривать.

Для связи со мной используйте e-mail: codecity@gmail.com, support@wmsigner.com.