import time
import uuid
import requests
import os
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities

import csv

#杀死进程命令： taskkill /im  chromedriver.exe /f

def get_menu(page):
    for k in range(page):
        time.sleep(5)
        wait.until(ec.presence_of_element_located((By.LINK_TEXT, "后页>")))
        tables = browser.find_elements(By.XPATH, '//div[@class="title"]/a[@class="title-text"]')
        for table in tables:
            link = table.get_attribute("href")
            title = table.text
            movies.append({title: link})
        browser.find_element(By.LINK_TEXT, "后页>").click()



def downloadimg(url, filename):
    pa = ''
    try:
        ttt = requests.get(url=url)
        pa = 'img/%s.jpg' % filename
        if os.path.exists('./%s' % pa):
            pa
        with open('./%s' % pa, 'wb') as file:
            file.write(ttt.content)
            print("%s.jpg已下载完成" % filename)
    except Exception as e:
        print('%s.jpg下载失败' % filename)
    return "/" + pa

def get_products(link):
    # 后退到百度页面
    browser2.back()
    browser2.get(link)

    time.sleep(5)

    # 等待页面加载
    wait2.until(ec.presence_of_element_located((By.CSS_SELECTOR, '#mainpic')))
    name = browser2.find_element(By.XPATH, '//div[@id="content"]/h1/span[1]').text.strip().replace("\r\n","").replace("\r","").replace("\n","").replace(",","，")
    # img = browser2.find_element(By.XPATH, '//*[@id="mainpic"]/a/img').get_attribute("src")
    # img = downloadimg(img,str(uuid.uuid1()).replace("-",""))
    # daoyan = browser2.find_element(By.XPATH, '//*[@id="info"]/span[1]/span[2]').text.strip().replace("\r\n","").replace("\r","").replace("\n","")
    types = browser2.find_elements(By.XPATH, '//div[@id="info"]/span[@property="v:genre"]')
    tt = []
    for tp in types:
        tt.append(tp.text.strip().replace("\r\n", "").replace("\r", "").replace("\n", "").replace(",","，"))
    # online = browser2.find_element(By.XPATH, '//*[@id="info"]/span[@property="v:runtime"]').text.strip().replace("\r\n","").replace("\r","").replace("\n","")
    try:
        year = browser2.find_element(By.XPATH, '//div[@id="info"]/span[@property="v:initialReleaseDate"]').text.strip().replace("\r\n","").replace("\r","").replace("\n","").replace(",","，")
    except Exception as ex:
        print('还未上映，无法获取上映时间跳过！')
        return
    score = browser2.find_element(By.XPATH, '//div[@id="interest_sectl"]/div[1]/div[2]/strong').text.strip().replace("\r\n","").replace("\r","").replace("\n","").replace(",","，")
    # small = browser2.find_element(By.XPATH, '//*[@id="link-report"]/span').text.strip().replace("\r\n","").replace("\r","").replace("\n","")
    #tables = browser2.find_elements(By.XPATH,'//*[@id="comments-section"]/div[2]/div[2]/div[1]/div')
    tables = browser2.find_elements(By.XPATH, '//div[@id="comments-section"]//div[@id="hot-comments"]/div')
    for com1 in tables:
        try:
            tm = com1.find_element(By.XPATH, './div/h3/span[2]/span[@class="comment-time "]').text.replace(",","，")
            content = com1.find_element(By.XPATH,'./div/p/span').text.replace(",","，")
            writer.writerow([name, "/".join(tt), year, score, tm, content])
            print(name, "/".join(tt), year, score, tm, content)
        except Exception as e:
            print(e)
            continue





if __name__ == "__main__":

    # 初始化浏览器对象
    chrome_options = Options()
    # 设置无窗口模式
    # chrome_options.add_argument('--headless')
    # 解决反爬识别selenium 避免浏览器监测
    chrome_options.add_experimental_option('excludeSwitches', ['enable-automation'])
    # 设置user-agent，来模拟客户端，如iPhone Safari浏览器
    chrome_options.add_argument(
        'user-agent="Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"')
    # 设置默认编码为 utf-8，也就是中文
    chrome_options.add_argument('lang=zh_CN.UTF-8')
    # 禁止图片加载
    prefs = {"profile.managed_default_content_settings.images": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    # 设置页面加载策略，none表示非阻塞模式。
    desired_capabilities = DesiredCapabilities.CHROME
    desired_capabilities["pageLoadStrategy"] = "none"

    # 避免浏览器监测
    opt = Options()
    opt.add_experimental_option("excludeSwitches", ['enable-automation'])
    # 停止页面的不必要加载
    #opt.page_load_strategy = 'eager'

    # 无头浏览器，在后台运行
    # opt.add_argument("--headless")
    browser = webdriver.Chrome(options=chrome_options, executable_path='C:/chromedriver.exe', desired_capabilities=desired_capabilities)
    #browser = webdriver.Chrome(options=opt, executable_path='C:/chromedriver.exe')
    # 显示等待，等待browser页面5秒
    wait = WebDriverWait(browser, 5)
    # 爬取链接
    movies = []

    