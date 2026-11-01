-- ============================================================
-- 医院 IT 运维平台 · D3 建库建表
-- 执行方式：MySQL Workbench 里打开本文件 → 点闪电图标执行
--           或命令行 mysql -u root -p < 01-asset.sql
-- ============================================================

-- 1. 建库（字符集必须是 utf8mb4，否则中文和 emoji 会变问号）
CREATE DATABASE IF NOT EXISTS hospital_ops
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE hospital_ops;

-- 2. 建表。DROP 是为了让你可以反复重跑这个脚本
DROP TABLE IF EXISTS asset;

CREATE TABLE asset (
  id                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  asset_code           VARCHAR(32)  NOT NULL                COMMENT '资产编号，如 ZC-0001',
  name                 VARCHAR(100) NOT NULL                COMMENT '设备名称',
  category             VARCHAR(32)  NOT NULL                COMMENT '类别：PC/PRINTER/SELF_SERVICE/SERVER/NETWORK/OTHER',
  dept                 VARCHAR(50)  NOT NULL                COMMENT '使用科室',
  location             VARCHAR(100) DEFAULT NULL            COMMENT '具体位置',
  critical_level       TINYINT      NOT NULL DEFAULT 2      COMMENT '重要级别：1高 2中 3低',
  status               VARCHAR(16)  NOT NULL DEFAULT 'RUNNING' COMMENT '状态：RUNNING正常/DEGRADED异常/DOWN停机/SCRAPPED报废',
  owner                VARCHAR(50)  DEFAULT NULL            COMMENT '责任人',
  last_maintained_on   DATE         DEFAULT NULL            COMMENT '上次维护日期',
  next_maintenance_on  DATE         DEFAULT NULL            COMMENT '下次计划维护日期',
  created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_asset_code (asset_code),
  KEY idx_dept (dept),
  KEY idx_status (status),
  KEY idx_next_maintenance (next_maintenance_on)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产台账';

-- 3. 灌 20 条假数据
--    故意做成有分布的样子：大部分正常，2 台异常，1 台停机，1 台报废，
--    2 台维护日期已过期（用于第 5 周做"到期提醒"功能）
INSERT INTO asset
  (asset_code, name, category, dept, location, critical_level, status, owner, last_maintained_on, next_maintenance_on)
VALUES
  ('ZC-0001','门诊收费电脑',        'PC',           '门诊收费处','门诊楼1层3号窗口',  2,'RUNNING', '张伟','2026-08-12','2027-02-12'),
  ('ZC-0002','自助挂号机（东）',    'SELF_SERVICE', '门诊大厅',  '门诊楼1层大厅东侧',  1,'RUNNING', '李静','2026-09-03','2027-03-03'),
  ('ZC-0003','自助挂号机（西）',    'SELF_SERVICE', '门诊大厅',  '门诊楼1层大厅西侧',  1,'DEGRADED','李静','2026-10-15','2027-04-15'),
  ('ZC-0004','激光打印机',          'PRINTER',      '门诊收费处','门诊楼1层3号窗口',  3,'RUNNING', '张伟','2026-06-20','2026-12-20'),
  ('ZC-0005','医生工作站电脑',      'PC',           '心内科',    '门诊楼3层12诊室',    2,'RUNNING', '王芳','2026-07-08','2027-01-08'),
  ('ZC-0006','PACS 阅片工作站',     'PC',           '放射科',    '医技楼2层阅片室',    1,'RUNNING', '刘强','2026-09-18','2027-03-18'),
  ('ZC-0007','检验条码打印机',      'PRINTER',      '检验科',    '医技楼1层标本接收处',2,'DEGRADED','陈明','2026-05-11','2026-11-11'),
  ('ZC-0008','药房叫号显示屏',      'OTHER',        '药剂科',    '门诊楼1层药房',      2,'RUNNING', '赵敏','2026-08-25','2027-02-25'),
  ('ZC-0009','住院护士站电脑',      'PC',           '骨科病区',  '住院部7层护士站',    2,'RUNNING', '孙丽','2026-04-30','2026-10-30'),
  ('ZC-0010','手术室信息终端',      'PC',           '手术室',    '住院部3层手术区',    1,'RUNNING', '周杰','2026-09-29','2027-03-29'),
  ('ZC-0011','医保读卡器',          'OTHER',        '门诊收费处','门诊楼1层3号窗口',  2,'RUNNING', '张伟','2026-07-14','2027-01-14'),
  ('ZC-0012','叫号系统服务器',      'SERVER',       '信息科',    '行政楼4层机房',      1,'RUNNING', '吴涛','2026-10-01','2027-04-01'),
  ('ZC-0013','核心交换机',          'NETWORK',      '信息科',    '行政楼4层机房',      1,'RUNNING', '吴涛','2026-10-01','2027-04-01'),
  ('ZC-0014','机房 UPS',            'OTHER',        '信息科',    '行政楼4层机房',      1,'DEGRADED','吴涛','2026-03-15','2026-09-15'),
  ('ZC-0015','无线 AP（大厅）',     'NETWORK',      '门诊大厅',  '门诊楼1层大厅',      2,'RUNNING', '吴涛','2026-06-06','2026-12-06'),
  ('ZC-0016','报告自助打印机',      'SELF_SERVICE', '医技楼',    '医技楼1层走廊',      2,'DOWN',    '陈明','2026-10-20','2027-04-20'),
  ('ZC-0017','超声科工作站',        'PC',           '超声科',    '医技楼3层3号诊室',   2,'RUNNING', '郑爽','2026-08-02','2027-02-02'),
  ('ZC-0018','门诊大厅 LED 屏',     'OTHER',        '门诊部',    '门诊楼1层大厅',      3,'RUNNING', '李静','2026-05-25','2026-11-25'),
  ('ZC-0019','手持扫码枪',          'OTHER',        '住院药房',  '住院部1层药房',      3,'SCRAPPED','赵敏','2024-11-10', NULL),
  ('ZC-0020','信息科测试机',        'PC',           '信息科',    '行政楼4层办公室',    3,'RUNNING', '吴涛','2026-09-09','2027-03-09');

-- 4. 验证（执行完看这三条的结果）
SELECT COUNT(*) AS 总数 FROM asset;                       -- 期望 20
SELECT status, COUNT(*) AS 数量 FROM asset GROUP BY status;  -- 期望 RUNNING 15 / DEGRADED 3 / DOWN 1 / SCRAPPED 1
SELECT asset_code, name, dept, next_maintenance_on
  FROM asset
 WHERE next_maintenance_on < CURDATE()
 ORDER BY next_maintenance_on;                            -- 期望 2 条（ZC-0009、ZC-0014）
