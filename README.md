#COP290 Assignment 0 : Team Registration App  
slack channel [https://cop290ninjacoders.slack.com/messages](https://cop290ninjacoders.slack.com/messages)

##TODO
###Overall  
* Improve UI
* Add tests (unit tests, etc.)   
* Add comments in code
	* Use Javadoc to create docs
* Build user manual / help
* Add credits page  

###Immediate
* Add icon of apk
* Design document
* Comment code and create javadocs
* Categorize class files into directories
	* restructure package
* Add submit button
* Add Welcome page
* Add confirmation page
* Add credits page
* merge kansal's validator branch.
* remove keyboard on valid response.
* ~~Add border around image if img from ldap is found.~~ (@r-ashish check this if done).
* merge kansal's validator branch.
* remove keyboard on valid response.
* ~~incase of invalid entry number (i.e. response of the form <h1> ()</h1>) reflect this in entry number input text box.~~


##Issues
* ~~First fill entry number - 2013ee10431.~~ (FIXED)(@abhishek_kedia check)(@r-ashish check out this video [https://youtu.be/bm_joteWJb8](https://youtu.be/bm_joteWJb8) for details of issue, @AK101111 add me to your google+ to view this.)
	1. Gets Abhishek Kedia's photo, name, entry number.
	2. Now click on edit this entry.
	3. Fill entry number - 2013ee10432.
	4. Gets Abhishek Anand's data. It has no photo.
	5. ~~Now rotatte screen. You get Abhishek Kedia's photo instead of no photo.~~
	6. ~~Now rotate screen. You get no photo, even the default photo.~~

* ~~Checking entry number field when name is empty.~~ (FIXED)(@r-ashish @AK101111 please verify)
	1. ~~Clear out name field, if there is anything already there.
	2. fill incomplete (and invalid) entry number.
	3. Click submit button.
	4. This does not give invalid entry number error.~~ 
* ~~Error in checking invalid entry number:~~ (FIXED)(@r-ashish @AK101111 please verify)
	1. ~~turn off internet.
	2. give incorrect entry number.
	3. click submit.
	4. ^gives error.
	5. now type a correct entrt number and press submit again. Does not work.~~

* ~~Entry number size not limit not restricted and not checking sometimes:(@abhishek_kedia : have you applied android:maxLength="11" property for entryCode editText ?) ~~ (Fixed) (@r-ashish, yes that was the issue. it was removed by a careless merge of mine.)
	1. Connect to internet.
	2. Enter entry number 2013EE10430. -> automatically checks that this is invalid as soon as you enter this. Also you cannot enter any more characters.
	3. ^ this is behavior is correct, and is expected always. But sometimes not respected as below.
	4. Enter 2013eez10430222222222 ... as many characters you want.
	5. ^ does not check / stop allowing more entries.

###New features
* Add suggestions from name also along with entry number.
* Generalise other type of form filling
	* Integrate with Google Docs & Survey Monkey