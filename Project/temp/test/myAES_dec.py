from Crypto.Cipher import AES
from Crypto.Hash import SHA256 as SHA
from os import path
import os

KSIZE = 1024

class _AES():
    def __init__(self, keytext, ivtext):
        hash = SHA.new()
        hash.update(keytext.encode('utf-8'))
        key = hash.digest()
        self.key = key[:16]

        hash.update(ivtext.encode('utf-8'))
        iv = hash.digest()
        self.iv = iv[:16]

    def makeEncInfo(self, filename):
        fillersize = 0
        filesize = path.getsize(filename)

        if filesize%16 != 0:
            fillersize = 16 - filesize%16

        filler = '0' * fillersize
        header = '%d' % (fillersize)
        gap = 16 - len(header)
        header += '#' * gap

        return header, filler

    def dec(self, encfilename):
        words = encfilename.split('.')
        del(words[-1])
        filename = '.'.join(words)

        encfilename = 'C:/Project/crypto/_encoded/' + encfilename
        filename = 'C:/Project/crypto/_decoded/' + filename
        
        aes = AES.new(self.key, AES.MODE_CBC, self.iv)
        try: os.remove(filename)
        except: pass
        h = open(filename, 'wb+')
        hh = open(encfilename, 'rb')

        content = hh.read(16)
        dec = aes.decrypt(content)
        try: header = dec.decode()
        except:
            h.close()
            os.remove(filename)
            h = open('C:/Project/crypto/_decoded/decodeError', 'wb')
            h.close()
            return -1
        fillersize = int(header.split('#')[0])

        content = hh.read(KSIZE)

        while content:
            try: dec = aes.decrypt(content)
            except:
                h.close()
                os.remove(filename)
                h = open('C:/Project/crypto/_decoded/decodeError', 'wb')
                h.close()
            
            if len(dec) < KSIZE:
                if fillersize != 0:
                    dec = dec[:-fillersize]
            h.write(dec)
            content = hh.read(KSIZE)

        h.close()
        hh.close()
        os.remove(encfilename)

def main():
    keytext = 'tdat'
    ivtext = 'tdat'
    
    try:
        with open('C:/Project/crypto/pw.txt', 'r') as f:
            text = f.read()
            if text != '':
                keytext = text
                ivtext = keytext[::-1]
        os.remove('C:/Project/crypto/pw.txt')
    except:
        pass
    
    myCipher = _AES(keytext, ivtext)

    file_list = os.listdir("C:/Project/crypto/_encoded")
    
    for encfilename in file_list:
        err = myCipher.dec(encfilename)
        if err == -1:
            return

    

if __name__ == '__main__':
    main()
        




        
