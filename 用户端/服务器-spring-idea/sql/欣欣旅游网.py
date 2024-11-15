import re
import csv      #进行csv操作
from pymysql import *
from bs4 import BeautifulSoup   #网页解析
import requests

headers={
        "scheme": "https",
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "accept-encoding": "gzip, deflate",
        "accept-language": "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6",
        "cache-control": "no-cache",
        "pragma": "no-cache",
        "sec-ch-ua": "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"91\", \"Chromium\";v=\"91\"",
        "sec-ch-ua-mobile": "?0",
        "sec-fetch-dest": "document",
        "sec-fetch-mode": "navigate",
        "sec-fetch-site": "none",
        "sec-fetch-user": "?1",
        "upgrade-insecure-requests": "1",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36 Edg/91.0.864.48"
}

# get数据请求
def getUrl(url):
    headers["Referer"] = url
    # headers["Cookie"] = "ll=\"118203\"; bid=wk0ixe6Hn48; __utmz=30149280.1655035613.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmz=223695111.1655035664.1.1.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; _vwo_uuid_v2=D188D860FD48F86CBE81A4C4290E37A1A|5a7e202be58f76a5cc5f63aa6a660b15; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1655040526%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_ses.100001.4cf6=*; __utma=30149280.1701518197.1655035613.1655035613.1655040526.2; __utmb=30149280.0.10.1655040526; __utmc=30149280; __utma=223695111.1620680889.1655035664.1655035664.1655040526.2; __utmb=223695111.0.10.1655040526; __utmc=223695111; ap_v=0,6.0; _pk_id.100001.4cf6=0adefdbda6f4df97.1655035664.2.1655043152.1655035731."
    try:
        resp = requests.get(url, headers=headers, timeout = 30)
        resp.raise_for_status()
        resp.encoding = resp.apparent_encoding  #根据网页编码解析解码
        return resp
    except:
        return None

#将list中的数据存储到csv文件中
def saveCsv(dictData,name,Tag = False):
    """
    保存的csv文件先用记事本打开，
    然后另存为ANSI编码的文件，
    最后用excel打开即可！！！
    :param dictData: 字典类型数据
    :param name: 保存的文件名
    :return: null
    """
    print("爬取的数据将保存在csv文件中..")
    with open(f'{name}.csv', 'a', encoding='utf-8',newline='') as csvfile:
        fp = csv.DictWriter(csvfile,fieldnames=dictData[0].keys())
        if Tag:
            fp.writeheader()
        try:
            fp.writerows(dictData)
        except Exception as e:
            print(e)



def get_guonei(num = 1,name = "旅游资讯"):
    # 创建Connection连接
    conn = connect(host='localhost', port=3306, database='login', user='root', password='123456', charset='utf8')
    # 获得Cursor对象
    cursor = conn.cursor()
    guonei_url = f"https://news.cncn.com/guonei/index{num}.htm"
    resp = getUrl(guonei_url).text
    html = BeautifulSoup(resp, "html.parser")
    uls = html.find_all("ul", class_="news_list")
    lists = []
    for ul in uls:
        lis = ul.find_all("li")
        for li in lis:
            news_url = re.findall('href="(.*?)"',str(li))[0]
            # lists.append(get_news(news_url))
            # print(news_url)
            try:
                news = get_news(news_url)
                lists.append(news)
                sql = "insert into news_table (news_title, news_content, news_source) VALUE("+"\'"+news["标题"]+"\'"+","+"\'"+news["内容"]+"\'"+","+"\'"+news["来源"]+"\'"+");"
                # print(sql)
                count = cursor.execute(sql)
                conn.commit()
                # print(count.fetchone())
            except Exception as e:
                print(e)

    # 关闭
    cursor.close()
    conn.close()
    if num == 0:
        saveCsv(lists, name,True)
    else:
        saveCsv(lists, name, False)




def get_news(url):
    news = {}
    resp = getUrl(url).text
    html = BeautifulSoup(resp, "html.parser")
    news_con = html.find("div", class_="news_con")
    news["标题"] = news_con.find("h1").text.replace("\u3000","")
    timeSoup = news_con.find("div",class_="time")
    news["时间"] = re.findall('"time"> (.*?) <span',str(timeSoup))[0]
    news["来源"] = timeSoup.find("span").text.replace("来源：","")
    news["内容"] = news_con.find("div",class_="con").text.replace("  ","").replace("<","").replace('"',"\"")
    news["url"] = url
    print(news)
    return news

if __name__ == '__main__':
    for i in range(1,20):
        get_guonei(i)