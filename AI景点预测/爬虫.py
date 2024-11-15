import json
import requests
import os
headers={
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.864.37"
}

def get_word(word,type = "train"):
    savepath = f"./{type}/temp/"
    if os.path.exists(savepath+word)==False:
        print("正在创建",savepath+word,"文件夹：")
        os.makedirs(savepath+word)
    if type == "train":
        strat_num = 1
        end_num = 5
    elif type == "val":
        strat_num = 6
        end_num = 7
    else:
        strat_num = 8
        end_num = 10
    number=0
    for i in range(strat_num,(int(end_num)+1)):
        print("=======正在爬取第",i,"页========")
        url = f"https://image.baidu.com/search/acjson?tn=resultjson_com&logid=11778711170918898776&ipn=rj&ct=201326592&is=&fp=result&queryWord={word}&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=&z=&ic=&hd=&latest=&copyright=&word={word}&s=&se=&tab=&width=&height=&face=&istype=&qc=&nc=1&fr=&expermode=&nojc=&pn={str(30 * i)}&rn=30&gsm=3c&1622726555368="
        try:
            resp = requests.get(url, headers=headers).json()
        except Exception as e:
            print(e)
        data_list = resp["data"]

        for lis in data_list[:-1]:
            name=lis["fromPageTitleEnc"]
            name = name.replace("/","").replace('"',"").replace("|","").replace("\\","").replace(":","").replace("*","").replace("<","").replace(">","").replace("?","").strip()
            tp_url=lis["middleURL"]
            print(number, "正在保存：",name)
            number+=1
            try:
                content = requests.get(tp_url, headers=headers).content
                with open(f"{savepath+word}/{number}.jpg","wb") as f:
                    f.write(content)
            except Exception as e:
                print(e)
    print(word,"爬取成功")


with open("name.json","r",encoding='utf8') as name:
    json_data = json.load(name)

for aword in json_data:
    # word = '大明山风景区'
    word = aword['景点']
    print(word)
    try:
        get_word(word, "train")
        get_word(word, "val")
        get_word(word, "test")
    except Exception as e:
        print(e)


