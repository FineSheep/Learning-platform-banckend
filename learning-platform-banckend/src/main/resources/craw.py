import requests
from lxml import etree

if __name__ == '__main__':
    User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36 Edg/98.0.1108.43"
    headers = {"User-Agent": User_Agent}
    for page in range(0, 2):
        if page == 0:
            url = "http://cyberpolice.mps.gov.cn/wfjb/html/bgl/index.shtml"
        else:
            url = "http://cyberpolice.mps.gov.cn/wfjb/html/bgl/index_" + str(page) + ".shtml"
        page_text = requests.get(url=url, headers=headers).content.decode('UTF-8')
        tree = etree.HTML(page_text)
        tr_list = tree.xpath(
            "/html/body/table[4]/tbody/tr/td/table[1]/tbody/tr/td[2]/table[3]/tbody/tr/td/table/tbody/tr")
        for tr in tr_list[0:-1]:
            title = "".join(tr.xpath("./td[1]/a/text()")[0].split())  # 标题
            time = tr.xpath("./td[2]/text()")[0][1:-1]  # 发表时间
            info_page_url = "http://cyberpolice.mps.gov.cn" + tr.xpath("./td[1]/a/@href")[0]
            info_page_text = requests.get(url=info_page_url, headers=headers).content.decode('UTF-8')
            info_tree = etree.HTML(info_page_text)
            source = info_tree.xpath(
                "/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/div/p[1]/span[1]/font/text()")
            if not source:
                source = info_tree.xpath(
                    "/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table[2]/tbody/tr/td/div[1]/span/span/text()")
                source = ["".join(source[0].split())]
            if not source[0]:
                source = info_tree.xpath(
                    "/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table[2]/tbody/tr/td/div[2]/span/span/text()")
            source = "".join(source[0].split())
            if len(source) > 30:
                source = "来源:未知"
            source = str(source).replace(":", '：').split("：")[-1]  # 来源
            info_list = info_tree.xpath("/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/div/p")
            info_text = ""
            for info_p in info_list[1:-1]:
                info_p_text = info_p.xpath("./span[1]//text()")
                p_text = ''.join(info_p_text)
                p_text = "".join(p_text.split()).replace("\n", '')
                if p_text:
                    info_text = info_text + p_text + "\n"
            if not info_text:
                info_list = info_tree.xpath(
                    "/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/table[2]/tbody/tr/td/div")
                info_text = ""
                for info_p in info_list[2:]:
                    info_p_text = info_p.xpath("./span/span/text()")
                    p_text = ''.join(info_p_text)
                    p_text = "".join(p_text.split()).replace("\n", '')
                    if p_text:
                        info_text = info_text + p_text + "\n"
            if not info_text:
                p_text = info_tree.xpath("/html/body/table[4]/tbody/tr/td/table[3]/tbody/tr/td/div/p[2]//text()")
                p_text = ''.join(p_text)
                info_text = "".join(p_text.split()).replace("\n", '')
            info_dict = {
                "title": title,
                "putTime": time,
                "source": source,
                "content": info_text,
                "link": info_page_url
            }
            text = str(info_dict).replace("'", '"')
            print(text)
