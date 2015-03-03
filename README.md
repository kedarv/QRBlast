# QRBlast

QRBlast is an innovative way to send small amounts of data over QR Codes, without the need of any networking. 
This app has two functions, sending and receiving data. To send data, we use the Android Storage Access Framework (SAF) to
pick a file from the device or the device's connected accounts. Next, the data is encoded using Base64, and then split up into
chunks of 750 characters. These characters are converted into QR codes, with pagination data prepended for the receiver. The app then
rotates through all of the generated QR Codes. Once the receiver phone has captured all the data, the data is concatenated and then displayed.
