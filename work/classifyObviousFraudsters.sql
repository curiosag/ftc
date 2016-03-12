UPDATE classifiedAds SET status = -1 
WHERE status = 0
and description CONTAINS '@outlook';

UPDATE classifiedAds SET status = -1 
WHERE status = 0
and description CONTAINS '@hotmail.com';

UPDATE classifiedAds SET status = -1 
WHERE status = 0
and description CONTAINS 'AUFMERKSAMKEIT';

UPDATE classifiedAds SET status = -1
WHERE status = 0 
and description CONTAINS 'WARTEN WEIL ICH HABE KEINE ZEIT';

UPDATE classifiedAds SET status = -1
WHERE status = 0 
and description CONTAINS 'KONTAKT DIREKT AN:';

UPDATE classifiedAds SET status = -1
WHERE status = 0 
and description CONTAINS 'WENDEN SIE SICH BITTE DIREKT UNTER:';

UPDATE classifiedAds SET status = -1
WHERE status = 0 
and description CONTAINS 'ALLE INFORMATIONEN DIREKT KONTAKT EMAIL:';



