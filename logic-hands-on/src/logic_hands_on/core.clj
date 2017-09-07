(ns logic-hands-on.core
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]))

(defn oy-fp-puffi-spec [spec]
  (l/fresh [start]
           (l/== spec [start :ravintolasali])
           (l/membero start [:13:20 :13:55 :14:55 :15:30 :16:05])))

(defn koodinlausunta-spec [spec]
  (l/fresh [place]
           (l/== spec [:18:45 place])
           (l/membero place [:mokki-x :mokki-y])))

(defn anyplace [place]
  (l/membero place [:ravintolasali :kabinetti :mokki-x :mokki-y]))

(defn anyplace-and-time [spec]
  (l/fresh [start place]
           (l/== spec [start place])
           (l/membero start [:13:30 :14:05 :15:05 :15:40 :16:15])
           (anyplace place)
           (l/!= spec [:13:30 :kabinetti])))

(defn early-spec [spec]
  (l/fresh [start]
           (anyplace-and-time spec)
           (l/firsto spec start)
           (l/membero start [:13:30 :14:05])))

(defn workshop-spec [spec]
  (l/fresh [start place]
           (l/== spec [start place])
           (anyplace-and-time spec)
           (l/membero place [:mokki-x :mokki-y])))

(defn long-happening-specs [spec1 spec2]
  (l/fresh [start1 start2 place]
           (l/== spec1 [start1 place])
           (l/== spec2 [start2 place])
           (l/membero [start1 start2]
                      [[:13:30 :14:05] [:15:05 :15:40] [:15:40 :16:15]])
           (anyplace place)))

(defn same-time [spec1 spec2]
  (l/fresh [start]
           (l/firsto spec1 start)
           (l/firsto spec2 start)))

(defn different-time [spec1 spec2]
  (l/fresh [start1 start2]
           (l/firsto spec1 start1)
           (l/firsto spec2 start2)
           (l/!= start1 start2)))

(defn schedule [sched]
  (l/fresh [oy-fp koodinlausunta livekoodaus flux nodejs tiedustelu
            dependent-type ai-testing generative-testing
            model-based-testing vuorovaikutus koodauskompo1 koodauskompo2
            powershell aspnet akkanet1 akkanet2]
           (l/distincto [livekoodaus flux nodejs
                         generative-testing model-based-testing
                         koodauskompo1 koodauskompo2 powershell aspnet
                         akkanet1 akkanet2])
           (early-spec livekoodaus)
           (anyplace-and-time flux)
           (early-spec nodejs)
           (different-time flux nodejs)
           (workshop-spec generative-testing)
           (workshop-spec model-based-testing)
           (same-time generative-testing model-based-testing)
           (long-happening-specs koodauskompo1 koodauskompo2)
           (different-time livekoodaus koodauskompo1)
           (different-time livekoodaus koodauskompo2)
           (anyplace-and-time powershell)
           (early-spec aspnet)
           (long-happening-specs akkanet1 akkanet2)
           (different-time aspnet akkanet1)
           (different-time aspnet akkanet2)
           (different-time aspnet powershell)
           (l/== sched [:livekoodaus livekoodaus
                        :flux flux
                        :nodejs nodejs
                        :generative-testing generative-testing
                        :model-based-testing model-based-testing
                        :koodauskompo koodauskompo1 koodauskompo2
                        :powershell powershell
                        :aspnet aspnet
                        :akkanet akkanet1 akkanet2])))

; (defn schedule [sched]
;   (l/fresh [oy-fp koodinlausunta livekoodaus flux nodejs tiedustelu
;             dependent-type ai-testing generative-testing
;             model-based-testing vuorovaikutus koodauskompo1 koodauskompo2
;             powershell aspnet akkanet1 akkanet2]
;            (l/distincto [oy-fp koodinlausunta livekoodaus flux nodejs
;                          tiedustelu dependent-type ai-testing
;                          generative-testing model-based-testing vuorovaikutus
;                          koodauskompo1 koodauskompo2 powershell aspnet
;                          akkanet1 akkanet2])
;            (oy-fp-puffi-spec oy-fp)
;            (koodinlausunta-spec koodinlausunta)
;            (workshop-spec livekoodaus)
;            (anyplace-and-time flux)
;            (anyplace-and-time nodejs)
;            (anyplace-and-time tiedustelu)
;            (anyplace-and-time dependent-type)
;            (anyplace-and-time ai-testing)
;            (workshop-spec generative-testing)
;            (workshop-spec model-based-testing)
;            (same-time generative-testing model-based-testing)
;            (anyplace-and-time vuorovaikutus)
;            (long-happening-specs koodauskompo1 koodauskompo2)
;            (anyplace-and-time powershell)
;            (anyplace-and-time aspnet)
;            (long-happening-specs akkanet1 akkanet2)
;            (different-time aspnet akkanet1)
;            (different-time aspnet akkanet2)
;            (l/== sched [:oy-fp oy-fp :flux flux :nodejs nodejs
;                         :koodinlausunta koodinlausunta
;                         :livekoodaus livekoodaus
;                         :tiedustelu tiedustelu
;                         :dependent-type dependent-type
;                         :ai-testing ai-testing
;                         :generative-testing generative-testing
;                         :model-based-testing model-based-testing
;                         :vuorovaikutus vuorovaikutus
;                         :koodauskompo koodauskompo1 koodauskompo2
;                         :powershell powershell
;                         :aspnet aspnet
;                         :akkanet akkanet1 akkanet2])))



